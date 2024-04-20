package com.example.portfolio.security;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("STOCK")
public class Stock extends Security {
    /**
     * Represents the expected return of the stock; a value between 0 and 1.
     * Denoted as `μ` in the Discrete Time Geometric Brownian Motion model.
     */
    @Column
    private double expectedReturn;

    /**
     * Represents the standard deviation of the stock; a value between 0 and 1.
     * Denoted as `σ` in the Discrete Time Geometric Brownian Motion model.
     */
    @Column
    private double standardDeviation;

    /**
     * If the application grows with more use-cases that load stocks without options, we can consider lazy fetching.
     * However, for the purposes of the assignment, we will use eager fetching to avoid the N+1 problem.
     */
    @OneToMany(mappedBy = "underlying", fetch = FetchType.EAGER)
    private List<Option> options;

    public double getExpectedReturn() {
        return expectedReturn;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public List<Option> getOptions() {
        return options;
    }
}
