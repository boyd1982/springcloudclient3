package com.example.springcloudclient3.controller;

import com.example.springcloudclient3.pojo.Product;
import com.example.springcloudclient3.service.ProductService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private RestTemplate restTemplate = null;
    @Autowired
    private ProductService productService = null;
    @GetMapping("/ribbon")
    public Product testRibbon(){
        Product prod = null;
        for (int i=0;i<10;i++){
            prod=restTemplate.getForObject("" +
                    "http://PRODUCT/product/"+(i+1),Product.class);
        }
        return prod;
    }
    @GetMapping("/feign")
    public Product testFeign(){
        Product product = null;
        for (int i=0;i<10;i++){
            Long id=(long) (i+1);
            product=productService.getProduct(id);
        }
        return product;
    }

    @GetMapping("/feign1")
    public Map<String,Object> testFeign2(){
        Map<String,Object> result=null;
        Product product = null;
        for (int i=1;i<=10;i++){
            Long id = (long) i;
            product = new Product();
            product.setId(id);
            int level = i%3+1;
            product.setUserName("product_name_"+id);
            product.setLevel(level);
            result = productService.addUser(product);
        }
        return result;
    }
    @GetMapping("/feign3")
    public Map<String,Object> testFeign3(){
        Map<String,Object> result = null;
        for (int i=0;i<10;i++){
            Long id=(long)(i+1);
            String productName = "product_name_"+id;
            result = productService.updateName(productName,id);
        }
        return result;
    }
//    ribbon熔断
    @GetMapping("/circuitBreaker1")
    @HystrixCommand(fallbackMethod = "error",commandProperties = {
            @HystrixProperty(
                    name = "execution.isolation.thread.timeoutInMilliseconds",
                    value = "2000"
            )
    })
    public  String circuitBreaker1(){
        return restTemplate.getForObject("http://PRODUCT/timeout",String.class);
    }

    @GetMapping("/circuitBreaker2")
    @HystrixCommand(fallbackMethod = "error")
    public String circuitBreaker2(){
        return productService.testTimeout();
    }
    public String error(){
        return "超时出错。";
    }

}
