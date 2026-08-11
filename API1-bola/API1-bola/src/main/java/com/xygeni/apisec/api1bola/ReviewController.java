package com.xygeni.apisec.api1bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ASEC-102 (rate_limit_absence, Java/Spring) fixture.
 *
 * POST /api/products/{id}/reviews is authenticated and accepts user input
 * (the review body), but there is no @RateLimiter, bucket4j, or
 * Resilience4j usage anywhere in this project -> rate_limit_absence,
 * severity High (write endpoint).
 */
@RestController
@RequestMapping("/api/products")
public class ReviewController {

    @PostMapping("/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    public void addReview(@PathVariable String id, @RequestBody String reviewText) {
        // persists the review
    }
}
