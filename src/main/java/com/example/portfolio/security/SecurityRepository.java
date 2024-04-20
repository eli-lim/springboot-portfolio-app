package com.example.portfolio.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityRepository extends JpaRepository<Security, Long> {
    Security findBySymbol(String symbol);
}
