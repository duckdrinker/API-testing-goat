package com.xygeni.apisec.api2auth;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
    public Balance getBalance(String accountId) {
        return new Balance(accountId, 1000.00);
    }
}
