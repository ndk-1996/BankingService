package com.banking.fintech.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Service health API", description = "Endpoint for service health check")
@RequestMapping("/")
public interface HealthController {

    @Operation(summary = "Check the health status of the service")
    @GetMapping()
    ResponseEntity<String> healthCheck();
}
