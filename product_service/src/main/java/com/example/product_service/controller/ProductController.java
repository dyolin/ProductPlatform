package com.example.product_service.controller;

import com.example.product_service.dto.request.ProductRequest;
import com.example.product_service.dto.response.BaseResponse;
import com.example.product_service.dto.response.ProductResponse;
import com.example.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class ProductController {

    private final ProductService productService;

    // CREATE PRODUCT
    @PostMapping
    public ResponseEntity<BaseResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request,
            @RequestHeader("X-Source") String source,
            @RequestHeader("X-User-Id") String username
    ) {

        log.info("X-Source: {}, X-User-Id: {}, Product Title: {}",
                source, username, request.getTitle());

        ProductResponse response = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.ok("Product created successfully", response));
    }

    // GET PRODUCT BY ID
    @GetMapping("/{id}")
    public BaseResponse<ProductResponse> getProductById(
            @PathVariable Long id) {

        ProductResponse response = productService.getProductById(id);

        return BaseResponse.ok("Product retrieved successfully", response);
    }

    //GET PRODUCT BY KEYWORD
    @GetMapping()
    public BaseResponse<List<ProductResponse>> searchProducts(
            @RequestParam("huruf") String huruf) {

        List<ProductResponse> response = productService.searchProductsByLetter(huruf);

        return BaseResponse.ok("Products retrieved successfully", response);
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    public BaseResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.updateProduct(id, request);

        return BaseResponse.ok("Product updated successfully", response);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return BaseResponse.ok("Product deleted successfully", null);
    }
}
