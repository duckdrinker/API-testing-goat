package com.xygeni.apisec.api5bfla;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BFLA negative fixture (Phase B "check present" branch).
 *
 * Same shape as AdminController.lockUser() (/admin/ path, administrative
 * operation name, authenticated), but the handler declares an explicit
 * role-bearing requirement (hasRole('ADMIN')) - Phase B should recognise
 * this pattern and clear the candidate instead of confirming it.
 */
@RestController
@RequestMapping("/api/admin")
public class SafeAdminController {

    private final UserAdminService userAdminService;

    public SafeAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @PostMapping("/users/{id}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public void unlockUser(@PathVariable String id) {
        userAdminService.unlock(id);
    }
}
