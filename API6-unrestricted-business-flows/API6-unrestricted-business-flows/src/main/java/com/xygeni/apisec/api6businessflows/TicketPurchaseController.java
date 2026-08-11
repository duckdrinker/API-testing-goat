package com.xygeni.apisec.api6businessflows;

import io.github.bucket4j.Bucket;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API6:2023 - Unrestricted Access to Sensitive Business Flows fixture.
 *
 * This endpoint is intentionally "clean" from the point of view of every
 * other Xygeni API Security detector: authentication is required, a
 * role-bearing check is present, and a rate limiter (bucket4j) is wired -
 * so it will NOT trigger unauthenticated_endpoint, BFLA, or
 * rate_limit_absence.
 *
 * The API6 risk this endpoint illustrates is at the *business logic*
 * level: a legitimate, authenticated, rate-limited user can still script
 * thousands of purchases per minute (up to the rate limit) to scalp
 * limited-availability tickets, because there is no business-flow-level
 * protection (e.g. purchase-velocity anomaly detection, CAPTCHA, per-event
 * purchase caps). No static source-code pattern captures this - it is not
 * a code-shape defect the way BOLA/BFLA/CORS/JWT misconfigurations are.
 *
 * As of this epic, Xygeni's API Security scanner has no detector for
 * API6:2023 (see the "Potential Improvements" section of the API Security
 * MVP epic). Scanning this fixture should produce zero API6-related
 * findings - that is the expected (documented) result, not a bug.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketPurchaseController {

    private final Bucket rateLimitBucket;

    public TicketPurchaseController(Bucket rateLimitBucket) {
        this.rateLimitBucket = rateLimitBucket;
    }

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String purchase(@RequestParam String eventId, @RequestParam int quantity) {
        if (!rateLimitBucket.tryConsume(1)) {
            return "rate limited";
        }
        return "purchased " + quantity + " ticket(s) for event " + eventId;
    }
}
