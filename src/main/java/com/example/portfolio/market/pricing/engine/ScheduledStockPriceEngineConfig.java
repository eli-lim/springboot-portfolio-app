package com.example.portfolio.market.pricing.engine;

import com.example.portfolio.market.pricing.store.PriceStore;
import com.example.portfolio.security.StockRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configures a dynamic scheduling task that calls {@link ScheduledStockPriceEngineTick} at random intervals.
 */
@Configuration
@EnableScheduling
class ScheduledStockPriceEngineConfig implements SchedulingConfigurer {

    private static final int MIN_INTERVAL_MS = 500;
    private static final int MAX_INTERVAL_MS = 2000;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PriceStore priceStore;
    private final StockRepository stockRepository;

    ScheduledStockPriceEngineConfig(
        ApplicationEventPublisher applicationEventPublisher,
        PriceStore priceStore,
        final StockRepository stockRepository
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.priceStore = priceStore;
        this.stockRepository = stockRepository;
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
            // AtomicInteger used to share the deltaT value between the task and the trigger.
            AtomicInteger deltaT = new AtomicInteger(0);

            taskRegistrar.addTriggerTask(
                new ScheduledStockPriceEngineTick(applicationEventPublisher, priceStore, stock, deltaT),
                (context) -> {
                    Optional<Date> lastCompletionTime = Optional.ofNullable(context.lastCompletionTime());
                    deltaT.set(randBetween(MIN_INTERVAL_MS, MAX_INTERVAL_MS));
                    Instant nextExecutionTime =
                        lastCompletionTime.orElseGet(Date::new)
                            .toInstant()
                            .plusMillis(deltaT.get());
                    return Date.from(nextExecutionTime);
                }
            );
        });
    }

    private int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }
}
