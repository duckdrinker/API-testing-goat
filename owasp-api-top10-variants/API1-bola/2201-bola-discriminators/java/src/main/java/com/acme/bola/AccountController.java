package com.acme.bola;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Case E: id_uses. The unchecked id reaches a lookup (getAccount) and a mutation
 * (deleteAccount); both lines must be reported. Case F (negative): the lookup is scoped
 * to the caller (findAccountByIdAndOwner), so there is no BOLA and no id_uses.
 */
@RestController
@RequestMapping("/api")
public class AccountController {

    private final Repo repo;

    public AccountController(Repo repo) {
        this.repo = repo;
    }

    @GetMapping("/accounts/{id}")
    @PreAuthorize("isAuthenticated()")
    public Account getAccount(@PathVariable String id) {
        return repo.findAccount(id);
    }

    @DeleteMapping("/accounts/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteAccount(@PathVariable String id) {
        repo.deleteAccount(id);
    }

    @GetMapping("/my/accounts/{id}")
    @PreAuthorize("isAuthenticated()")
    public Account getMyAccount(@PathVariable String id, java.security.Principal principal) {
        return repo.findAccountByIdAndOwner(id, principal.getName());
    }
}