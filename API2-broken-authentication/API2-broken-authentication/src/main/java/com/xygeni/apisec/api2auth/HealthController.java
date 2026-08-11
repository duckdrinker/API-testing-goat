package com.xygeni.apisec.api2auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * unauthenticated_endpoint negative fixture (public-path heuristic).
 *
 * No authentication here either, but the path matches the recognised
 * public-path heuristic (/health) - the detector should suppress the
 * finding for this endpoint.
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
