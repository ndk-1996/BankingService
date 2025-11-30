package com.banking.fintech.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthControllerImpl implements HealthController {

    @Override
    public ResponseEntity<String> healthCheck() {
        log.info("Health check endpoint called");

        return ResponseEntity.ok("Service is up and running. Hit /swagger-ui.html endpoint to view the APIs exposed by this service");
    }
}
