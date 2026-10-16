package fx;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public final class JwtUtil {
    private static final java.security.PublicKey KEY = null;

    private JwtUtil() { }

    public static Claims decode(String token) {
        return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload();
    }
}
