package com.example.portfolio.position;

import com.example.portfolio.security.Security;
import com.example.portfolio.security.SecurityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides the positions from a CSV file.
 * The positions are loaded from the CSV file when the application starts.
 * In reality, this likely would be dynamic, instead of some static value.
 */
@Component
public class PositionProviderCsv implements PositionProvider {

    private static final Logger LOG = LoggerFactory.getLogger(PositionProviderCsv.class.getName());

    private final ResourceLoader resourceLoader;

    private final SecurityRepository securityRepository;

    private List<Position> positions = null;
    private final Set<String> symbols = new HashSet<>();

    public PositionProviderCsv(
        final ResourceLoader resourceLoader,
        final SecurityRepository securityRepository
    ) {
        this.resourceLoader = resourceLoader;
        this.securityRepository = securityRepository;
    }

    @Override
    public List<Position> listPositions() {
        // Load the positions from the CSV file if they haven't been loaded yet
        // In reality, if `positions` are ever-changing, they would likely be loaded from a database or some other
        // dynamic source.
        if (positions == null) {
            positions = readPositionsFromCsv();
        }
        return positions;
    }

    @Override
    public boolean hasPosition(String symbol) {
        return symbols.contains(symbol);
    }

    private List<Position> readPositionsFromCsv() {
        // Read the positions from the CSV file
        List<List<String>> records = new ArrayList<>();

        final Resource fileResource = resourceLoader.getResource("classpath:positions.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(fileResource.getFile()))) {
            LOG.info("Reading positions from file: {}", fileResource.getURL());

            // Skip the header row
            br.readLine();

            // Read the remaining rows
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            LOG.error("File not found");
        } catch (Exception e) {
            LOG.error("Error reading positions from CSV file");
        }

        // Convert the records to Position objects
        List<Position> positions = records
            .stream()
            .map(record -> {
                String symbol = record.get(0);
                long positionSize = Long.parseLong(record.get(1));

                Security security = securityRepository.findBySymbol(symbol);

                // If the security is not found, log an error and return null
                if (security == null) {
                    LOG.error("Security not found for symbol: {}", symbol);
                    return null;
                }

                return new Position(security, positionSize);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        // Cache the symbols for quick lookup
        positions.forEach(position -> symbols.add(position.getSecurity().getSymbol()));
        LOG.info("Loaded {} positions", positions.size());

        return positions;
    }
}
