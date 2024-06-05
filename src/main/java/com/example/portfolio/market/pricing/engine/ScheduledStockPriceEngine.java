package com.example.portfolio.market.pricing.engine;

import com.example.portfolio.market.pricing.PriceTickEvent;
import com.example.portfolio.market.pricing.engine.models.BlackScholesEuropeanOptionsPricing;
import com.example.portfolio.market.pricing.engine.models.GeometricBrownianMotionPricing;
import com.example.portfolio.market.pricing.store.PriceStore;
import com.example.portfolio.security.Option;
import com.example.portfolio.security.OptionType;
import com.example.portfolio.security.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Scheduled to run at random intervals to update the price of a stock and its options.
 */
@Component
public class ScheduledStockPriceEngine implements PriceEngine {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledStockPriceEngine.class);

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PriceStore priceStore;

    public ScheduledStockPriceEngine(
        final ApplicationEventPublisher applicationEventPublisher,
        final PriceStore priceStore
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.priceStore = priceStore;
    }

    @Override
    public void onTick(final Stock stock, final TimeStep timeStep) {
        long dt = timeStep.getDeltaMs();
        long epochMilli = timeStep.getLast().toEpochMilli();

        // Update stock price
        double lastPrice = priceStore.getPrice(stock);
        double nextPrice = GeometricBrownianMotionPricing.price(stock.getExpectedReturn(), stock.getStandardDeviation(), lastPrice, dt);
        priceStore.setPrice(stock, nextPrice);

        // Update option prices
        stock.getOptions().forEach(option -> priceStore.setPrice(option, computeOptionPrice(option, epochMilli)));

        LOG.info("Stock {} | t {} -> {} | p {} -> {}", stock.getSymbol(), timeStep.getLast().toEpochMilli(), timeStep.getCurrent().toEpochMilli(), lastPrice, nextPrice);

        // Publish the price tick event
        applicationEventPublisher.publishEvent(
            new PriceTickEvent(this, stock.getSymbol(), lastPrice, nextPrice, dt));
    }

    private double computeOptionPrice(Option option, long epochMilli) {
        // Time to expiration in years
        Date expiration = option.getExpiration();
        double yearInMs = 31536000000.0;

        double t = (expiration.getTime() - epochMilli) / yearInMs;

        // Constant risk-free rate at 2%
        double r = 0.02;
        double k = option.getStrike();

        // Get the current price of the underlying stock
        Stock underlying = option.getUnderlying();
        double s = priceStore.getPrice(underlying);
        double sigma = underlying.getStandardDeviation();

        double price = OptionType.CALL == option.getOptionType()
            ? BlackScholesEuropeanOptionsPricing.callPrice(s, k, r, sigma, t)
            : BlackScholesEuropeanOptionsPricing.putPrice(s, k, r, sigma, t);

        return price;
    }
}
