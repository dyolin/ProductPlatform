package com.example.scheduler_service.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DummyJsonResponse {
    private List<DummyJsonProductResponse> products;
    private Integer total;
    private Integer skip;
    private Integer limit;
}
