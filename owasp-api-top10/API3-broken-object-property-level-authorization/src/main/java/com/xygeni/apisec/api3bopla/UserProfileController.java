package com.xygeni.apisec.api3bopla;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * API3:2023 - Broken Object Property Level Authorization fixture.
 *
 * No authentication annotation anywhere on the /profile methods below:
 *  - GET accepts a sensitive "ssn" parameter with no auth requirement
 *    -> sensitive_param_unauthenticated
 *  - GET's response DTO exposes "ssn" (unreferenced by the request,
 *    sensitivity-tagged) -> excessive_data_exposure + pii_leak_in_response
 *  - PUT accepts a "role" field that binds through to the persistent User
 *    entity -> mass_assignment
 *
 * The remaining methods are the negative counterparts (see SecurityConfig
 * for why these are the ones that actually require authentication).
 */
@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserProfileResponse getProfile(@RequestParam String ssn) {
        return userService.findBySsn(ssn);
    }

    @PutMapping("/profile")
    public UserProfileResponse updateProfile(@RequestBody UserProfileUpdateRequest update) {
        return userService.update(update);
    }

    /** sensitive_param_unauthenticated negative: same "ssn" param shape, but authenticated. */
    @GetMapping("/secure-profile")
    @PreAuthorize("isAuthenticated()")
    public UserProfileResponse getSecureProfile(@RequestParam String ssn) {
        return userService.secureFindBySsn(ssn);
    }

    /** excessive_data_exposure / pii_leak_in_response negative: no unreferenced sensitive field. */
    @GetMapping("/{id}/summary")
    @PreAuthorize("isAuthenticated()")
    public UserSummaryResponse getSummary(@PathVariable String id) {
        return userService.summary(id);
    }

    /** mass_assignment negative: request DTO has no bindable protected field. */
    @PutMapping("/{id}/safe-update")
    @PreAuthorize("isAuthenticated()")
    public UserProfileResponse safeUpdate(@PathVariable String id, @RequestBody UserProfileSafeUpdateRequest update) {
        return userService.safeUpdate(id, update);
    }
}
