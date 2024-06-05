package com.example.portfolio.market.pricing.engine.models;

public class BlackScholesEuropeanOptionsPricing {

    /**
     * Calculates the price of a European option using the Black-Scholes formula.
     *
     * @param spotPrice             The current price of the underlying asset.
     * @param strikePrice           The price at which the option holder can buy or sell the underlying asset.
     * @param riskFreeRate          The risk-free interest rate.
     * @param volatility            The standard deviation of the underlying asset's returns.
     * @param timeToMaturityInYears The time until the option expires in years.
     * @return The price of the option.
     */
    public static double callPrice(
        final double spotPrice,
        final double strikePrice,
        final double riskFreeRate,
        final double volatility,
        final double timeToMaturityInYears
    ) {
        return price(spotPrice, strikePrice, riskFreeRate, volatility, timeToMaturityInYears, true);
    }

    /**
     * Calculates the price of a European option using the Black-Scholes formula.
     *
     * @param spotPrice             The current price of the underlying asset.
     * @param strikePrice           The price at which the option holder can buy or sell the underlying asset.
     * @param riskFreeRate          The risk-free interest rate.
     * @param volatility            The standard deviation of the underlying asset's returns.
     * @param timeToMaturityInYears The time until the option expires in years.
     * @return The price of the option.
     */
    public static double putPrice(
        final double spotPrice,
        final double strikePrice,
        final double riskFreeRate,
        final double volatility,
        final double timeToMaturityInYears
    ) {
        return price(spotPrice, strikePrice, riskFreeRate, volatility, timeToMaturityInYears, false);
    }

    private static double price(
        final double spotPrice,
        final double strikePrice,
        final double riskFreeRate,
        final double volatility,
        final double timeToMaturityInYears,
        final boolean isCall
    ) {
        double S = spotPrice;
        double K = strikePrice;
        double r = riskFreeRate;
        double sigma = volatility;
        double T = timeToMaturityInYears;

        double d1 = (Math.log(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        if (isCall) {
            return S * NormalDistribution.cdf(d1) - K * Math.exp(-r * T) * NormalDistribution.cdf(d2);
        }

        return K * Math.exp(-r * T) * NormalDistribution.cdf(-d2) - S * NormalDistribution.cdf(-d1);
    }
}
