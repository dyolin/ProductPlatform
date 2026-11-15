package com.example.scheduler_service.client;

import com.example.scheduler_service.dto.response.DummyJsonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dummy-json-client", url = "${dummyjson.url}")
public interface DummyJsonClient {

    @GetMapping("/products/category/smartphones")
    DummyJsonResponse getSmartphones(@RequestParam("limit") int limit, @RequestParam("skip") int skip);
}