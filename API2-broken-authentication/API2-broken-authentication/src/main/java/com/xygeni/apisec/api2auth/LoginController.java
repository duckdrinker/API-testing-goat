package com.xygeni.apisec.api2auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ASEC-033 (sensitivity classifier, CREDENTIAL tag) fixture.
 *
 * password is a credential-shaped parameter name -> the SensitivityClassifier
 * should tag it CREDENTIAL, CWE-522, regardless of any other detector this
 * endpoint's shape (login, necessarily unauthenticated) might also trigger.
 */
@RestController
public class LoginController {

    @PostMapping("/api/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return "token";
    }
}
