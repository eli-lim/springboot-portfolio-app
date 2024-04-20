package com.example.portfolio.security;

import javax.persistence.*;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Table(name="security")
@Inheritance(strategy=SINGLE_TABLE)
@DiscriminatorColumn(name="securityType", discriminatorType=STRING, length=20)
public class Security {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "symbol", nullable = false, unique = true)
    private String symbol;

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }
}

