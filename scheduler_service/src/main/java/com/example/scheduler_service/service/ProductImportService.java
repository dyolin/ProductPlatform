package com.example.scheduler_service.service;

import com.example.scheduler_service.client.DummyJsonClient;
import com.example.scheduler_service.client.ProductClient;
import com.example.scheduler_service.dto.request.ProductImportRequest;
import com.example.scheduler_service.dto.response.DummyJsonProductResponse;
import com.example.scheduler_service.dto.response.DummyJsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__( @Autowired))
public class ProductImportService {

    private final DummyJsonClient dummyJsonClient;
    private final ProductClient productClient;

    public void importSmartphones() {

        // 1. Fetch smartphone products from DummyJSON
        DummyJsonResponse response = dummyJsonClient.getSmartphones();

        if (response == null || response.getProducts() == null) {
            log.warn("No products received from DummyJSON");
            return;
        }

        // 2. Convert DummyJSON DTOs into Product Service request DTOs
        List<ProductImportRequest> products = new ArrayList<>();

        for (DummyJsonProductResponse item : response.getProducts()) {

            ProductImportRequest request = new ProductImportRequest();

            request.setDummyJsonId((long) item.getId());
            request.setTitle(item.getTitle());
            request.setDescription(item.getDescription());
            request.setCategory(item.getCategory());
            request.setBrand(item.getBrand());
            request.setPrice(item.getPrice());
            request.setDiscountPercentage(item.getDiscountPercentage());
            request.setRating(item.getRating());
            request.setStock(item.getStock());
            request.setThumbnail(item.getThumbnail());

            products.add(request);
        }

        // 3. Send products to Product Service through Feign
        productClient.importProducts(products);

        log.info("Successfully sent {} smartphone products to Product Service", products.size());
    }
}
