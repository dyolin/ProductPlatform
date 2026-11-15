package com.example.scheduler_service.client;

import com.example.scheduler_service.dto.request.ProductImportRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @PostMapping("/internal/products/import") //ke product-service controller
    void importProducts(@RequestBody List<ProductImportRequest> products);
}
