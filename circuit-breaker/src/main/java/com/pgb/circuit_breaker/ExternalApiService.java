package com.pgb.circuit_breaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class ExternalApiService {



    @CircuitBreaker(name = "myServiceCB", fallbackMethod = "fallback")
    public String callApi() {

        // Simulate random failure to trigger circuit breaker
        System.out.println("Calling API 02");
        try {
            Thread.sleep(1);
        } catch (Exception e) {
            System.err.println("Error");
        }

        Logger log = LoggerFactory.getLogger(getClass());
        log.debug("TEST DEBUG LOG");

        if (Math.random() < 0.1) {
            throw new RuntimeException("API failed");
        }
        return "Success!";
    }

    // fallback must match signature
    public String fallback(Throwable t) {
        System.out.println("fallback");
        return "Fallback response due to: " + t.getMessage();
    }
}
