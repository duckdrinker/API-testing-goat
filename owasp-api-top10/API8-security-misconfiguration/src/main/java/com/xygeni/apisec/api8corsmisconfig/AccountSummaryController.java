package com.xygeni.apisec.api8corsmisconfig;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API8:2023 - Security Misconfiguration fixture (CORS misconfiguration), High branch.
 *
 * @CrossOrigin(origins = "*", allowCredentials = "true") -> wildcard combined with
 * credentials, the genuinely dangerous combination -> cors_misconfiguration, severity High.
 *
 * Kept in its own file/controller (separate from PublicStatusController) - see that
 * class's javadoc for why.
 */
@RestController
@RequestMapping("/api/public")
public class AccountSummaryController {

    @GetMapping("/account-summary")
    @CrossOrigin(origins = "*", allowCredentials = "true")
    public String accountSummary() {
        return "{...account summary...}";
    }
}
