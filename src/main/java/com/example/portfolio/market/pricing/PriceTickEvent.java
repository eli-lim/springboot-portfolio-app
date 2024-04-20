package com.example.portfolio.market.pricing;

import org.springframework.context.ApplicationEvent;

/**
 * Event that is published when the price of a security is updated.
 */
public class PriceTickEvent extends ApplicationEvent {

    private final String symbol;
    private final double oldPrice;
    private final double newPrice;
    private final int intervalMs;

    public PriceTickEvent(Object source, String symbol, double oldPrice, double newPrice, int intervalMs) {
        super(source);
        this.symbol = symbol;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.intervalMs = intervalMs;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public int getIntervalMs() {
        return intervalMs;
    }
}
