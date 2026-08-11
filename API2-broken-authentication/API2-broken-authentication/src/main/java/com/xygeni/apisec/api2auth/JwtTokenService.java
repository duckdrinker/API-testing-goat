package com.xygeni.apisec.api2auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API2:2023 - Broken Authentication fixture (JWT misconfiguration).
 *
 * The verifier is built with Algorithm.none(), meaning it accepts tokens
 * with no signature at all - any attacker-crafted JWT with alg=none is
 * treated as valid. jwt_misconfiguration only scans an endpoint's own
 * handler file, so the vulnerable verification logic must live directly
 * in this controller method rather than in a separately-called service.
 */
@RestController
public class JwtTokenService {

    @PostMapping("/api/token/verify")
    public String verify(@RequestParam String token) {
        Algorithm algorithm = Algorithm.none();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getSubject();
    }
}
