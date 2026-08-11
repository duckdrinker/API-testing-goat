package com.xygeni.apisec.api8corsmisconfig;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API8:2023 - Security Misconfiguration fixture (CORS misconfiguration), Low branch.
 *
 * @CrossOrigin with no explicit "origins" -> wildcard default, no credentials
 * -> cors_misconfiguration, severity Low.
 *
 * Kept in its own file/controller (separate from AccountSummaryController) because
 * cors_misconfiguration analyses each handler file as a whole and reports once per
 * module - two different CORS declarations in the same file would collapse into a
 * single combined (High) finding instead of two distinct severities.
 */
@RestController
@RequestMapping("/api/public")
public class PublicStatusController {

    @GetMapping("/status")
    @CrossOrigin
    public String status() {
        return "ok";
    }
}
