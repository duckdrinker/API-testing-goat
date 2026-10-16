package fx;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public final class JwtUtil {
    private JwtUtil() { }

    public static Claims decode(String token) {
        return Jwts.parser().unsecured().build().parseUnsecuredClaims(token).getPayload();
    }
}
