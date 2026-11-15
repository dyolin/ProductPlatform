package com.example.scheduler_service.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductImportRequest {

    private Long dummyJsonId;
    private String title;
    private String description;
    private String category;
    private String brand;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private BigDecimal rating;
    private Integer stock;
    private String thumbnail;
}
