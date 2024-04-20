package com.example.portfolio.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query(value = "SELECT * FROM security WHERE security_type = 'STOCK' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Stock getRandomStock();

    Stock findBySymbol(String symbol);
}
