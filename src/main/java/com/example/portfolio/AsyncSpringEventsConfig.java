package com.example.portfolio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration class to enable asynchronous Spring events.
 * This is necessary to allow the PortfolioService to listen to PriceUpdateEvents asynchronously,
 * and to process the portfolio's positions in a separate thread.
 */
@Configuration
public class AsyncSpringEventsConfig {
    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
            new SimpleApplicationEventMulticaster();

        // Instead of using SimpleAsyncTaskExecutor, we use a ThreadPoolTaskExecutor
        // to make use of thread re-use; reduces overhead of creating new threads.
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("portfolio-");
        taskExecutor.initialize();
        eventMulticaster.setTaskExecutor(taskExecutor);
        return eventMulticaster;
    }
}
