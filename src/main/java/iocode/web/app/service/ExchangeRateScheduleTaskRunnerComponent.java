package iocode.web.app.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This component is responsible for running a scheduled task to fetch exchange rates from a currency API.
 * The task is executed every 12 hours.
 *
 * @author YourName
 */
@Component
public class ExchangeRateScheduleTaskRunnerComponent implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduleTaskRunnerComponent.class);
    private final ExchangeRateService rateService;
    private final ScheduledExecutorService scheduler;

    /**
     * Constructor for the ExchangeRateScheduleTaskRunnerComponent.
     *
     * @param rateService The service responsible for fetching exchange rates.
     * @param scheduler The executor service for scheduling the task.
     */
    public ExchangeRateScheduleTaskRunnerComponent(ExchangeRateService rateService, ScheduledExecutorService scheduler) {
        this.rateService = rateService;
        this.scheduler = scheduler;
    }

    /**
     * This method is executed when the application starts.
     * It schedules a task to fetch exchange rates from the currency API every 12 hours.
     *
     * @param args Command line arguments.
     * @throws Exception If an error occurs during the execution of the task.
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("Calling The Currency API endpoint for exchange rate");
        scheduler.scheduleWithFixedDelay(rateService::getExchangeRate, 0, 12, TimeUnit.HOURS);
        logger.info("Ended Calling The Currency API endpoint");
    }
}
