package com.example.portfolio.portfolio;

import com.example.portfolio.market.pricing.PriceProvider;
import com.example.portfolio.market.pricing.PriceTickEvent;
import com.example.portfolio.position.Position;
import com.example.portfolio.position.PositionProvider;
import com.example.portfolio.security.Security;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Subscribes to price updates and logs the portfolio's positions and their respective market values.
 */
@Component
public class PortfolioPrinter {

    private final PositionProvider positionProvider;
    private final PriceProvider priceProvider;

    public PortfolioPrinter(
        final PositionProvider positionProvider,
        final PriceProvider priceProvider
    ) {
        this.positionProvider = positionProvider;
        this.priceProvider = priceProvider;
    }

    @Async
    @EventListener
    public void handlePriceTick(@NonNull final PriceTickEvent event) {
        System.out.println();
        System.out.println("Market Data Update");
        System.out.printf(
            "\n%s from %s to %s over %sms\n%n",
            event.getSymbol(), event.getOldPrice(), event.getNewPrice(), event.getIntervalMs());
        System.out.println(buildPositionsTable());
    }

    /**
     * Builds a table of the portfolio's positions and their respective market values.
     *
     * @return the built table to be printed to the console.
     */
    String buildPositionsTable() {
        StringBuilder sb = new StringBuilder();

        // Calculate the total value of the portfolio
        double totalValue = 0.0;

        // Log the table header
        String format = "%30s%30s%30s%30s%n";
        sb.append("\n");
        sb.append(String.format(format, "## Portfolio", "", "", ""));
        sb.append(String.format(format, "symbol", "price", "qty", "value"));

        // Log the positions
        final List<Position> positions = positionProvider.listPositions();
        for (Position position : positions) {
            Security security = position.getSecurity();
            double price = priceProvider.getMarketPrice(security);
            double mtmValue = priceProvider.getMarketPrice(security) * position.getSize();

            sb.append(String.format(format,
                position.getSecurity().getSymbol(),
                price,
                position.getSize(),
                mtmValue
            ));

            totalValue += mtmValue;
        }

        // Log the total value of the portfolio
        sb.append(String.format(format, "# Total portfolio", "", "", totalValue));
        sb.append("\n");
        return sb.toString();
    }
}
