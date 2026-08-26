package com.xygeni.apisec.api2auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * jwt_misconfiguration negative fixture.
 *
 * The algorithm is explicitly pinned (HMAC256), the secret is sourced
 * from the environment (not hardcoded), and expiration validation is
 * left at its default (enabled) - none of the misconfiguration patterns
 * this detector looks for are present. Kept as its own endpoint (not a
 * plain @Service) for the same reason as JwtTokenService: the detector
 * only scans an endpoint's own handler file.
 */
@RestController
public class SafeJwtTokenService {

    private final Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));

    @PostMapping("/api/token/verify-safe")
    public String verify(@RequestParam String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getSubject();
    }
}
