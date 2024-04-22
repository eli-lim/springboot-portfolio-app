package com.example.portfolio.market.pricing.engine;

import com.example.portfolio.security.Stock;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public class Tick implements Runnable, Trigger {

    private final PriceEngine engine;

    private final Stock stock;

    private TimeStep timeStep;

    public Tick(PriceEngine engine, Stock stock) {
        this.engine = engine;
        this.stock = stock;
        this.timeStep = TimeStep.fromNow();
    }

    @Override
    public void run() {
        engine.onTick(stock, timeStep);
    }

    @Override
    public Date nextExecutionTime(TriggerContext context) {
        Optional<Date> lastCompletionTime = Optional.ofNullable(context.lastCompletionTime());

        // Advance the time step by a random interval between 0.5s - 2s
        int deltaT = randBetween(500, 2000);
        timeStep = timeStep.advance(deltaT);

        Instant nextExecutionTime =
            lastCompletionTime.orElseGet(Date::new)
                .toInstant()
                .plusMillis(deltaT);

        return Date.from(nextExecutionTime);
    }

    private int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
}
