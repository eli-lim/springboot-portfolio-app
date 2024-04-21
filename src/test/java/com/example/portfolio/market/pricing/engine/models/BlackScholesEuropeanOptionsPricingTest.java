package com.example.portfolio.market.pricing.engine.models;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlackScholesEuropeanOptionsPricingTest {

    @ParameterizedTest(name = "spotPrice={0} strikePrice={1} riskFreeRate={2} volatility={3} timeToMaturityInYears={4} expectedCall={5} expectedPut={6}")
    @MethodSource("optionsPriceArgs")
    void shouldCalculateOptionPriceCorrectly(
        final double spotPrice,
        final double strikePrice,
        final double riskFreeRate,
        final double volatility,
        final double timeToMaturityInYears,
        double expectedCall,
        double expectedPut
    ) {
        assertEquals(expectedCall, BlackScholesEuropeanOptionsPricing.callPrice(spotPrice, strikePrice, riskFreeRate, volatility, timeToMaturityInYears), 1e-9);
        assertEquals(expectedPut, BlackScholesEuropeanOptionsPricing.putPrice(spotPrice, strikePrice, riskFreeRate, volatility, timeToMaturityInYears), 1e-9);
    }

    /**
     * @see <a href="https://www.omnicalculator.com/finance/black-scholes">Black Scholes Calculator<a>
     */
    static private Stream<Arguments> optionsPriceArgs() {
        return Stream.of(
            // spotPrice=390 strikePrice=400 riskFreeRate=0.02 volatility=0.3 timeToMaturityInYears=0.5
            Arguments.of(390, 400, 0.02, 0.3, 0.5, 30.273779962859464, 36.29371346252668),

            // spotPrice=100 strikePrice=110 riskFreeRate=0.02 volatility=0.5 timeToMaturityInYears=0.2
            Arguments.of(100, 110, 0.02, 0.5, 0.2, 5.3104032672794546, 14.871282095118524)
        );
    }
}
