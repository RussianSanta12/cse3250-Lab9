package com.pgb.circuit_breaker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final ExternalApiService service;

    public HelloController(ExternalApiService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public String testCircuitBreaker() {
        return service.callApi();
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from Spring Boot circuit breaker example!";
    }
}

