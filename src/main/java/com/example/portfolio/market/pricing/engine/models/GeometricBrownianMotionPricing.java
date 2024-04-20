package com.example.portfolio.market.pricing.engine.models;

import java.util.Random;

public class GeometricBrownianMotionPricing {

    /**
     * Calculates the next price of a stock using the Geometric Brownian Motion model.
     * @param expectedReturn The expected return of the stock, denoted by μ.
     * @param standardDeviation The standard deviation of the stock, denoted by σ.
     * @param lastPrice The last price of the stock.
     * @param dtMs The time interval in ms, denoted by Δt.
     */
    public static double price(
        final double expectedReturn,
        final double standardDeviation,
        final double lastPrice,
        final int dtMs
    ) {
        double mu = expectedReturn; // μ
        double sigma = standardDeviation; // σ

        // ε is a random variable that is drawn from a standardized normal distribution every time this formula is invoked.
        double epsilon = new Random().nextGaussian();

        double dtSecs = (double) dtMs / 1000;
        double dt = dtSecs / 7_257_600;
        double a = mu * dt;
        double b = sigma * epsilon * Math.sqrt(dt);

        double nextPrice = lastPrice * (a + b) + lastPrice;
        return nextPrice;
    }
}
