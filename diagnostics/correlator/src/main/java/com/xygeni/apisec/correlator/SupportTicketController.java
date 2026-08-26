package com.xygeni.apisec.correlator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * FlawCorrelator rule 2: zombie_endpoint_with_sensitive_param.
 * No auth annotation + sensitive "email" parameter -> sensitive_param_unauthenticated.
 * Intentionally absent from the companion openapi.yaml -> zombie_endpoint.
 * Both on the same endpoint -> the composite rule fires (severity high).
 */
@RestController
@RequestMapping("/api/internal")
public class SupportTicketController {

    @GetMapping("/support-ticket")
    public String getSupportTicket(@RequestParam String email) {
        return "{...support ticket history for " + email + "...}";
    }
}
