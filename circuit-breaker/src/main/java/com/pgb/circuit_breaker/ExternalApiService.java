package com.pgb.circuit_breaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ExternalApiService {

    private final AtomicInteger callCount = new AtomicInteger(0);
    private final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());

    @CircuitBreaker(name = "myServiceCB", fallbackMethod = "fallback")
    public String callApi() {

        long now = System.currentTimeMillis();
        long start = windowStart.get();
        if (now - start > 2000) {
            windowStart.set(now);
            callCount.set(0);
        }

        int count = callCount.incrementAndGet();
        if (count > 50) {
            throw new RuntimeException("Rate limit exceeded");
        }
        

        System.out.println("Calling API 02");
        try {
            Thread.sleep(1);
        } catch (Exception e) {
            System.err.println("Error");
        }

        Logger log = LoggerFactory.getLogger(getClass());
        log.debug("TEST DEBUG LOG");


        /*
        if (Math.random() > 0.5) {
            throw new RuntimeException("API failed");
        }
        */
        return "Success!";
    }

    // fallback must match signature
    public String fallback(Throwable t) {
        System.out.println("fallback");
        return "Fallback response due to: " + t.getMessage();
    }
}
