package com.example.portfolio.market.pricing.engine;

import com.example.portfolio.security.Stock;

public interface PriceEngine {
    void onTick(Stock stock, TimeStep timeStep);
}
