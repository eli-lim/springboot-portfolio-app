package com.example.portfolio.market.pricing.engine;

import com.example.portfolio.market.pricing.store.PriceStore;
import com.example.portfolio.security.StockRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

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

    private static final int MIN_INTERVAL_MS = 500;
    private static final int MAX_INTERVAL_MS = 2000;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PriceStore priceStore;
    private final StockRepository stockRepository;
    private final ScheduledStockPriceEngine engine;

    ScheduledStockPriceEngineConfig(
        final ApplicationEventPublisher applicationEventPublisher,
        final PriceStore priceStore,
        final StockRepository stockRepository,
        final ScheduledStockPriceEngine engine
    ) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.priceStore = priceStore;
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
}
