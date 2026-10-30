package com.acme.bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Case C: tenant-level BOLA. {orgId} is followed by more segments, so it partitions the
 * collection (scope); {projectId} is the last id, so it selects the row (object).
 * The lookup uses only projectId: orgId is never checked against the project.
 */
@RestController
@RequestMapping("/api/orgs/{orgId}/projects")
public class ProjectController {

    private final Repo repo;

    public ProjectController(Repo repo) {
        this.repo = repo;
    }

    @GetMapping("/{projectId}")
    @PreAuthorize("isAuthenticated()")
    public Project getProject(@PathVariable String orgId, @PathVariable String projectId) {
        return repo.findProject(projectId);
    }

    @PutMapping("/{projectId}")
    @PreAuthorize("isAuthenticated()")
    public Project updateProject(@PathVariable String orgId, @PathVariable String projectId, @RequestBody Project body) {
        return repo.saveProject(projectId, body);
    }
}