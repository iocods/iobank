package iocode.web.app.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ExchangeRateScheduleTaskRunnerComponent implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduleTaskRunnerComponent.class);
    private final ExchangeRateService rateService;
    private final ScheduledExecutorService scheduler;

    public ExchangeRateScheduleTaskRunnerComponent(ExchangeRateService rateService, ScheduledExecutorService scheduler) {
        this.rateService = rateService;
        this.scheduler = scheduler;
    }


    @Override
    public void run(String... args) throws Exception {
        logger.info("Calling The Currency API endpoint for exchange rate");
        scheduler.scheduleWithFixedDelay(rateService::getExchangeRate, 0, 12, TimeUnit.HOURS);
        logger.info("Ended Calling The Currency API endpoint");
    }
}
