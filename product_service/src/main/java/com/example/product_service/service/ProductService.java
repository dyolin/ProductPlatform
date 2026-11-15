package com.example.product_service.service;

import com.example.product_service.dto.request.ProductRequest;
import com.example.product_service.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> searchProductsByLetter(String huruf);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
