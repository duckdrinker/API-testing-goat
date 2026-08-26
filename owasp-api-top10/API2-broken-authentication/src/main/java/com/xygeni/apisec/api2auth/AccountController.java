package com.xygeni.apisec.api2auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API2:2023 - Broken Authentication fixture.
 *
 * No per-method or per-class auth annotation on this controller. Note this
 * path IS also listed in SecurityConfig's centralized permitAll() rule (added
 * later for the ASEC-045 known-limitation fixture) - but the
 * unauthenticated_endpoint detector doesn't parse that centralized config
 * either way (see ASEC-045), so from its point of view this endpoint still
 * has no security signal at all, same as if SecurityConfig didn't exist. The
 * path is not one of the recognised public-path heuristics (/health,
 * /metrics, /actuator/**, /.well-known/**), so it should not be excluded from
 * the unauthenticated_endpoint detector on that basis either.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam String accountId) {
        return accountService.getBalance(accountId);
    }
}
