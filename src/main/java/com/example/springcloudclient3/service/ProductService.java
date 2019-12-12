package com.example.springcloudclient3.service;

import com.example.springcloudclient3.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("product")
public interface ProductService {
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long id);
    @PostMapping("/insert")
    public Map<String,Object> addUser(@RequestBody Product product);
    @PostMapping("/update/{productName}")
    public Map<String,Object> updateName(
            @PathVariable("productName") String productName,
            @RequestHeader("id") Long id
    );
    @GetMapping("/timeout")
    public String testTimeout();
}
