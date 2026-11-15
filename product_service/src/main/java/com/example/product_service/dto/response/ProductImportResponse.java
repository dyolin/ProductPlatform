package com.example.product_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductImportResponse {

    private int totalReceived;
    private int imported;
    private int rejected;
    private int skipped;
}
