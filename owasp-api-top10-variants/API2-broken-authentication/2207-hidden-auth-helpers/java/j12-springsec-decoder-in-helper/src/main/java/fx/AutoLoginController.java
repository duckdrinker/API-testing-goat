package fx;

import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutoLoginController {
    private final JwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri("https://idp.example/jwks").build();

    private Jwt verify(String token) {
        return decoder.decode(token);
    }

    @PostMapping("/auto-login")
    public String autoLogin(@RequestHeader("X-Token") String token) {
        Jwt jwt = verify(token);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, List.of()));
        return "ok";
    }
}
