package com.example.portfolio.market.pricing.engine;

import com.example.portfolio.market.pricing.store.PriceStore;
import com.example.portfolio.market.pricing.PriceTickEvent;
import com.example.portfolio.market.pricing.engine.models.BlackScholesEuropeanOptionsPricing;
import com.example.portfolio.market.pricing.engine.models.GeometricBrownianMotionPricing;
import com.example.portfolio.security.*;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Scheduled to run at random intervals to update the price of a stock and its options.
 */
class ScheduledStockPriceEngineTick implements Runnable {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PriceStore priceStore;
    private final Stock stock;
    private final AtomicInteger deltaT;

    public ScheduledStockPriceEngineTick(
        ApplicationEventPublisher applicationEventPublisher,
        PriceStore priceStore,
        Stock stock,
        AtomicInteger deltaT
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.priceStore = priceStore;
        this.stock = stock;
        this.deltaT = deltaT;
    }

    @Override
    public void run() {
        int dt = deltaT.get();
        long epochMilli = Instant.now().toEpochMilli();

        // Update stock price
        double lastPrice = priceStore.getPrice(stock);
        double nextPrice = GeometricBrownianMotionPricing.price(stock.getExpectedReturn(), stock.getStandardDeviation(), lastPrice, dt);
        priceStore.setPrice(stock, nextPrice);

        // Update option prices
        stock.getOptions().forEach(option -> priceStore.setPrice(option, computeOptionPrice(option, epochMilli)));

        // Publish the price tick event
        applicationEventPublisher.publishEvent(
            new PriceTickEvent(this, stock.getSymbol(), lastPrice, nextPrice, dt)
        );
    }

    private double computeOptionPrice(Option option, long now) {
        // Time to expiration in years
        Date expiration = option.getExpiration();
        double yearInMs = 31536000000.0;

        double t = (expiration.getTime() - now) / yearInMs;

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
