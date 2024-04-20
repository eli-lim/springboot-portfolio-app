package com.example.portfolio.market.pricing;

import com.example.portfolio.security.Security;

public interface PriceProvider {
    double getMarketPrice(Security security);
}
