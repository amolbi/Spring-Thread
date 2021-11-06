package com.techshard.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Bean (name = "taskExecutor")
    public Executor taskExecutor() {
        LOGGER.debug("Creating Async Task Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("CarThread-");
        executor.initialize();
        System.out.println("AsyncConfiguration.taskExecutor()"+executor.getActiveCount());
        
        /*int NUMBER_OF_THREADS = 4;
        int NUMBER_OF_ITEMS = 12;
        ExecutorService exec = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        List<Item> items = getItems();
        int minItemsPerThread = NUMBER_OF_ITEMS / NUMBER_OF_THREADS;
        int maxItemsPerThread = minItemsPerThread + 1;
        int threadsWithMaxItems = NUMBER_OF_ITEMS - NUMBER_OF_THREADS * minItemsPerThread;
        int start = 0;
        List<Future<?>> futures = new ArrayList<Future<?>>(NUMBER_OF_ITEMS);
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            int itemsCount = (i < threadsWithMaxItems ? maxItemsPerThread : minItemsPerThread);
            int end = start + itemsCount;
            Runnable r = new Processor(items.subList(start, end));
            futures.add(exec.submit(r));
            start = end;
        }
        for (Future<?> f : futures) {
            f.get();
        }
        LOGGER.info("all items processed");*/
        return executor;
    }

}
