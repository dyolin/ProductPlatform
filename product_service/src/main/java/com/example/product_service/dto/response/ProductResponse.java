package com.example.product_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String brand;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private BigDecimal rating;
    private Integer stock;
    private String thumbnail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
