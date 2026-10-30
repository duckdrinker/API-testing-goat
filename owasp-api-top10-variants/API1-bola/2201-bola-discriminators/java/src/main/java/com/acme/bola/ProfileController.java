package com.acme.bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Case B: BOLA read whose response type has NO sensitive fields. */
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final Repo repo;

    public ProfileController(Repo repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public PublicProfile getProfile(@PathVariable String id) {
        return repo.findProfile(id);
    }
}