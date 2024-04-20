package com.example.portfolio.market.pricing;

import com.example.portfolio.market.pricing.store.PriceStore;
import com.example.portfolio.security.Security;
import org.springframework.stereotype.Service;

/**
 * Public API for retrieving market prices of securities.
 */
@Service
public class MarketPriceProvider implements PriceProvider {

    private final PriceStore priceStore;

    MarketPriceProvider(final PriceStore priceStore) {
        this.priceStore = priceStore;
    }

    @Override
    public double getMarketPrice(Security security) {
        return priceStore.getPrice(security);
    }
}
