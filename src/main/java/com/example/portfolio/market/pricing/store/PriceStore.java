package com.example.portfolio.market.pricing.store;

import com.example.portfolio.security.Security;

public interface PriceStore {
    double getPrice(Security security);

    void setPrice(Security security, double price);
}
