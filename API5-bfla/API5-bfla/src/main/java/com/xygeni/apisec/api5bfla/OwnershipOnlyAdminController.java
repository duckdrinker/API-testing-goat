package com.xygeni.apisec.api5bfla;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * BFLA fixture for the mixed-evidence case (xygeni/xygeni-product-backlog#1401).
 *
 * The handler proves OWNERSHIP and nothing else: the returned list is scoped to the
 * authenticated caller through a dataflow-bound findByOwner(authentication) call. Nothing
 * anywhere establishes that this class of caller may list users at all - there is no
 * role-bearing requirement, not even isAuthenticated() at the method level. Before the fix,
 * this ownership evidence was misread as also answering the function-level question and the
 * BFLA candidate on this admin-path listing endpoint was suppressed; it must now be confirmed
 * instead.
 */
@RestController
@RequestMapping("/api/admin")
public class OwnershipOnlyAdminController {

    private final UserAccountRepository userAccountRepository;

    public OwnershipOnlyAdminController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping("/users")
    public List<UserAccount> listUsers(Authentication authentication) {
        return userAccountRepository.findByOwner(authentication);
    }
}
