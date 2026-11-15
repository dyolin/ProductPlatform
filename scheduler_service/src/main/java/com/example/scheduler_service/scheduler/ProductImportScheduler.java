package com.example.scheduler_service.scheduler;

import com.example.scheduler_service.service.ProductImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductImportScheduler {

    private final ProductImportService productImportService;

    // Run immediately after application startup
    @EventListener(ApplicationReadyEvent.class)
    public void importOnStartup() {
        log.info("Starting initial smartphone product import...");
        runImport();
    }

    // Run every 2 minutes
    @Scheduled(cron = "${report.scheduler.cron}")
    public void importEveryTwoMinutes() {
        log.info("Starting scheduled smartphone product import on thread: {}", Thread.currentThread().getName());
        runImport();
    }

    private void runImport() {
        try {
            productImportService.importSmartphones();
        } catch (Exception e) {
            log.error("Product import failed. The next scheduled run will retry.", e);
        }
    }
}
