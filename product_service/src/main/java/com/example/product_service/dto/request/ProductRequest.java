package com.example.product_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Product title is required")
    @Size(max = 255)
    private String title;

    private String description;

    private String category;

    private String brand;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Product price must be greater than zero")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private BigDecimal discountPercentage;

    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    private BigDecimal rating;

    @PositiveOrZero(message = "Stock cannot be negative")
    private Integer stock;

    private String thumbnail;
}
