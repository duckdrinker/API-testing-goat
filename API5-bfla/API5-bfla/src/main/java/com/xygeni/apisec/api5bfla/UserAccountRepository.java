package com.xygeni.apisec.api5bfla;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAccountRepository {
    public List<UserAccount> findByOwner(Authentication owner) {
        return List.of();
    }
}
