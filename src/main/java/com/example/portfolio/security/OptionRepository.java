package com.example.portfolio.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository<T extends Option> extends JpaRepository<T, Long> {
    List<T> findByUnderlying(Stock stock);
}
