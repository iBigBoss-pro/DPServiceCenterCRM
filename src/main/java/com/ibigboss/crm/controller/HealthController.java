package com.ibigboss.crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Minimal endpoint to verify that the Spring Boot process is alive. */
@RestController
public class HealthController {
    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "application", "DPServiceCenterCRM");
    }
}
