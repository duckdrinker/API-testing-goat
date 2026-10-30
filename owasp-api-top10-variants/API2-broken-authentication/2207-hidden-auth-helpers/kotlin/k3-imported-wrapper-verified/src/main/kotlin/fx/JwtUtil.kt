package fx

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts

object JwtUtil {
    private val key: java.security.PublicKey? = null

    fun decode(token: String): Claims =
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
}
