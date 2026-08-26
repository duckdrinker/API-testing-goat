package com.xygeni.apisec.api5bfla;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API5:2023 - Broken Function Level Authorization fixture.
 *
 * The path contains "/admin/" and the operation name ("lock") is an
 * administrative/destructive action, but the only security requirement is
 * generic authentication (isAuthenticated()) - there is no role-bearing
 * requirement such as hasRole('ADMIN') or @Secured("ROLE_ADMIN").
 * Any authenticated (non-admin) user can lock any other user's account.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserAdminService userAdminService;

    public AdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @PostMapping("/users/{id}/lock")
    @PreAuthorize("isAuthenticated()")
    public void lockUser(@PathVariable String id) {
        userAdminService.lock(id);
    }
}
