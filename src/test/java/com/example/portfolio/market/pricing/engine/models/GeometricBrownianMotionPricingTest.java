package com.example.portfolio.market.pricing.engine.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeometricBrownianMotionPricingTest {

    @BeforeAll
    static void setUp() {
        // Set the seed for the random number generator for deterministic tests
        GeometricBrownianMotionPricing.setSeed(1L);
    }

    @Test
    void should_calculate_next_price() {
        double expectedReturn = 0.1;
        double standardDeviation = 0.2;
        double lastPrice = 100.0;
        int dtMs = 1000;

        // For seed 1L, epsilon first 5 values are:

        // ε = 1.561581040188955
        assertEquals(100.01159443587007, GeometricBrownianMotionPricing.price(expectedReturn, standardDeviation, lastPrice, dtMs), 0.0001);

        // ε = -0.6081826070068602
        assertEquals(99.99548627678656, GeometricBrownianMotionPricing.price(expectedReturn, standardDeviation, lastPrice, dtMs), 0.0001);

        // ε = -1.0912278829447088
        assertEquals(99.99190018566581, GeometricBrownianMotionPricing.price(expectedReturn, standardDeviation, lastPrice, dtMs), 0.0001);

        // ε = -0.6245401364066232
        assertEquals(99.99536483974151, GeometricBrownianMotionPricing.price(expectedReturn, standardDeviation, lastPrice, dtMs), 0.0001);

        // ε = -1.1182832102556484
        assertEquals(99.99169932898866, GeometricBrownianMotionPricing.price(expectedReturn, standardDeviation, lastPrice, dtMs), 0.0001);
    }
}
