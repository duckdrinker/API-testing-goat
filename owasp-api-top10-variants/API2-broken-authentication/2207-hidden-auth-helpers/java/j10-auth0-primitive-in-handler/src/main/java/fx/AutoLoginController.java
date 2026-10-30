package fx;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutoLoginController {
    @PostMapping("/auto-login")
    public String autoLogin(@RequestHeader("X-Token") String token) {
        DecodedJWT decoded = JWT.require(Algorithm.HMAC256("secret")).build().verify(token);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(decoded.getSubject(), null, List.of()));
        return "ok";
    }
}
