package com.example.portfolio.market.pricing.store;

import com.example.portfolio.security.Security;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * An in-memory implementation of {@link PriceStore}.
 * This class is thread-safe and allows for concurrent reads and writes.
 * In reality, this would be replaced with a durable, high-performance, key-value store like Redis.
 */
@Component
class InMemoryPriceStore implements PriceStore {

    /**
     * A data structure that stores the current price of each security.
     * The key is the symbol of the security and the value is the price.
     * This is a thread-safe map that allows for concurrent reads and writes.
     * In reality, this would likely be a highly performant store like Redis that allows for batched atomic updates.
     */
    private final ConcurrentMap<String, Double> priceCache = new ConcurrentHashMap<>();

    public InMemoryPriceStore() {
        setupStartingPrices();
    }

    @Override
    public double getPrice(Security security) {
        return priceCache.getOrDefault(security.getSymbol(), 0.);
    }

    @Override
    public void setPrice(Security security, double price) {
        priceCache.put(security.getSymbol(), price);
    }

    /**
     * For the purposes of this assignment, we will initialize the prices of AAPL, TSLA and NFLX.
     * In reality, these prices would already exist in the price store.
     */
    private void setupStartingPrices() {
        priceCache.putIfAbsent("AAPL", 110.0);
        priceCache.putIfAbsent("TSLA", 390.0);
        priceCache.putIfAbsent("NFLX", 550.0);
    }
}
