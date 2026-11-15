package com.example.product_service.controller;

import com.example.product_service.dto.request.ProductImportRequest;
import com.example.product_service.dto.response.BaseResponse;
import com.example.product_service.dto.response.ProductImportResponse;
import com.example.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class InternalProductController {

    private final ProductService productService;

    @PostMapping("/import")
    public BaseResponse<ProductImportResponse> importProducts(
            @RequestBody List<ProductImportRequest> products) {

        ProductImportResponse response = productService.importProducts(products);
        log.info("Total received: {}, imported: {}, skipped: {}, rejected: {}", response.getTotalReceived(), response.getImported(), response.getSkipped(), response.getRejected());
        return BaseResponse.ok("Products imported successfully", response);
    }
}
