package com.example.product_service.service.impl;

import com.example.product_service.dto.request.ProductRequest;
import com.example.product_service.dto.response.ProductResponse;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        mapRequestToEntity(request, product);

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> searchProductsByLetter(String huruf) {

        List<Product> products = productRepository.findByTitleContainingIgnoreCase(huruf);

        List<ProductResponse> result = new ArrayList<>();

        for (Product product : products) {
            result.add(mapToResponse(product));
        }

        return result;
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        mapRequestToEntity(request, product);

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));

        productRepository.delete(product);
    }

    private void mapRequestToEntity(ProductRequest request, Product product) {
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setDiscountPercentage(request.getDiscountPercentage());
        product.setRating(request.getRating());
        product.setStock(request.getStock());
        product.setThumbnail(request.getThumbnail());
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setBrand(product.getBrand());
        response.setPrice(product.getPrice());
        response.setDiscountPercentage(product.getDiscountPercentage());
        response.setRating(product.getRating());
        response.setStock(product.getStock());
        response.setThumbnail(product.getThumbnail());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }
}
