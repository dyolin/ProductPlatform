package com.example.scheduler_service.controller;

import com.example.scheduler_service.dto.response.BaseResponse;
import com.example.scheduler_service.service.ProductImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/scheduler")
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class SchedulerController {

    private final ProductImportService productImportService;

    @PostMapping("/import")
    public BaseResponse<String> importProducts() {

        productImportService.importSmartphones();
        log.info("Manual product import success");
        return BaseResponse.ok("Manual product import completed", "Smartphone products imported successfully" );
    }
}
