package com.example.portfolio.portfolio;

import com.example.portfolio.market.pricing.engine.PriceEngine;
import com.example.portfolio.market.pricing.engine.TimeStep;
import com.example.portfolio.market.pricing.engine.models.GeometricBrownianMotionPricing;
import com.example.portfolio.security.Stock;
import com.example.portfolio.security.StockRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The most valuable test in the application. It tests the application's core features, covering:
 * 1. the pricing calculations
 * 2. the portfolio positions table generation
 * We pay the price of some setup complexity (to make the test suite deterministic)
 * 1. seeding the random number generator
 * 2. mocking the system clock for deterministic time-based calculations
 */
@TestPropertySource(properties = "app.scheduling.enable=false") // Disable interval ticks for tests
@SpringBootTest
class PortfolioPrinterTest {

    @Autowired
    private PortfolioPrinter portfolioPrinter;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PriceEngine engine;

    @BeforeAll
    static void setUp() {
        GeometricBrownianMotionPricing.setSeed(1L);
    }

    @Test
    void should_build_positions_table_with_correct_values_and_format() {
        // Given the following positions
        String expectedTable =
            "\n" +
                "                  ## Portfolio                                                                                          \n" +
                "                        symbol                         price                           qty                         value\n" +
                "                          AAPL                         110.0                          1000                      110000.0\n" +
                "           AAPL-OCT-2024-110-C                           0.0                        -20000                          -0.0\n" +
                "           AAPL-OCT-2024-110-P                           0.0                         20000                           0.0\n" +
                "                          TSLA                         390.0                          -500                     -195000.0\n" +
                "           TSLA-NOV-2024-400-C                           0.0                         10000                           0.0\n" +
                "           TSLA-DEC-2024-400-P                           0.0                        -10000                          -0.0\n" +
                "             # Total portfolio                                                                                  -85000.0\n" +
                "\n";
        assertEquals(expectedTable, portfolioPrinter.buildPositionsTable());

        // When the price of AAPL ticks 1000ms
        Stock stock = stockRepository.findBySymbol("AAPL");
        engine.onTick(
            stock,
            new TimeStep(
                Instant.parse("2024-04-21T00:00:00Z"), Instant.parse("2024-04-21T00:00:01Z")));

        // Then the generated table should be updated with the new values
        expectedTable =
            "\n" +
                "                  ## Portfolio                                                                                          \n" +
                "                        symbol                         price                           qty                         value\n" +
                "                          AAPL            110.03188242516384                          1000            110031.88242516384\n" +
                "           AAPL-OCT-2024-110-C             15.84110346819947                        -20000            -316822.0693639894\n" +
                "           AAPL-OCT-2024-110-P            14.726638256812265                         20000             294532.7651362453\n" +
                "                          TSLA                         390.0                          -500                     -195000.0\n" +
                "           TSLA-NOV-2024-400-C                           0.0                         10000                           0.0\n" +
                "           TSLA-DEC-2024-400-P                           0.0                        -10000                          -0.0\n" +
                "             # Total portfolio                                                                       -107257.42180258027\n" +
                "\n";
        assertEquals(expectedTable, portfolioPrinter.buildPositionsTable());
    }
}
