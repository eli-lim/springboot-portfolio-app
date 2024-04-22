package com.example.portfolio.market.pricing.engine;

import com.example.portfolio.security.Stock;
import com.example.portfolio.security.StockRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Configures a dynamic scheduling task that calls {@link ScheduledStockPriceEngine} at random intervals.
 * We add a ConditionalOnProperty annotation to enable or disable the scheduling task so that the app can be
 * deterministically controlled in our automated tests.
 */
@ConditionalOnProperty(
    value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true
)
@Configuration
@EnableScheduling
class ScheduledStockPriceEngineConfig implements SchedulingConfigurer {

    private final StockRepository stockRepository;
    private final ScheduledStockPriceEngine engine;

    ScheduledStockPriceEngineConfig(
        final StockRepository stockRepository,
        final ScheduledStockPriceEngine engine
    ) {
        this.stockRepository = stockRepository;
        this.engine = engine;
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());

        // For each stock, create a scheduled task to update the price at random intervals.
        stockRepository.findAll().forEach(stock -> {
            Tick tick = new Tick(engine, stock);
            taskRegistrar.addTriggerTask(tick, tick);
        });
    }

    /**
     * A scheduled task that updates the price of a stock at random intervals.
     */
    private static class Tick implements Runnable, Trigger {

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
}
