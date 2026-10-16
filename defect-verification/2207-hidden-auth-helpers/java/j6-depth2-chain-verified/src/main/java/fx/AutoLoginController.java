package fx;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutoLoginController {
    private final java.security.PublicKey key = null;

    private Claims parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    private String subjectFrom(String token) {
        return parse(token).getSubject();
    }

    @PostMapping("/auto-login")
    public String autoLogin(@RequestHeader("X-Token") String token) {
        String subject = subjectFrom(token);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(subject, null, List.of()));
        return "ok";
    }
}
