package com.acme.bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Case D: same shape as case C with parameter names that a name heuristic does not know
 * (siteId, cid). The roles must come from the path structure: siteId = scope, cid = object.
 */
@RestController
@RequestMapping("/api/sites/{siteId}/devices")
public class SiteController {

    private final Repo repo;

    public SiteController(Repo repo) {
        this.repo = repo;
    }

    @GetMapping("/{cid}")
    @PreAuthorize("isAuthenticated()")
    public Device getDevice(@PathVariable String siteId, @PathVariable String cid) {
        return repo.findDevice(cid);
    }
}