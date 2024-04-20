package com.example.portfolio.portfolio;

import com.example.portfolio.market.pricing.PriceProvider;
import com.example.portfolio.market.pricing.PriceTickEvent;
import com.example.portfolio.position.Position;
import com.example.portfolio.position.PositionProvider;
import com.example.portfolio.security.Security;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Subscribes to price updates and logs the portfolio's positions and their respective market values.
 */
@Service
public class PortfolioPrinter implements ApplicationListener<PriceTickEvent> {

    private final PositionProvider positionProvider;
    private final PriceProvider priceProvider;

    public PortfolioPrinter(
        final PositionProvider positionProvider,
        final PriceProvider priceProvider
    ) {
        this.positionProvider = positionProvider;
        this.priceProvider = priceProvider;
    }

    @Override
    public void onApplicationEvent(@NonNull final PriceTickEvent event) {
        final List<Position> positions = positionProvider.listPositions();

        // Calculate the total value of the portfolio
        double totalValue = 0.0;

        // Pretty print the portfolio's positions
        System.out.println();
        System.out.println("Market Data Update");
        System.out.println(
           event.getSymbol() + " from " + event.getOldPrice() + " to " + event.getNewPrice() + " over " + event.getIntervalMs() + "ms"
        );

        if (!positionProvider.hasPosition(event.getSymbol())) {
            // The price update is not for a security in the portfolio
            return;
        }

        String format = "%30s%30s%30s%30s%n";
        System.out.format(format, "## Portfolio", "", "", "");
        System.out.format(format, "symbol", "price", "qty", "value");
        for (Position position : positions) {
            // Log each position
            Security security = position.getSecurity();
            double price = priceProvider.getMarketPrice(security);
            double mtmValue = priceProvider.getMarketPrice(security) * position.getSize();

            System.out.format(format,
                position.getSecurity().getSymbol(),
                price,
                position.getSize(),
                mtmValue
            );

            totalValue += mtmValue;
        }
        System.out.format(format, "# Total portfolio", "", "", totalValue);
        System.out.println();
    }
}
