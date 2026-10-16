package fx

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts

object JwtUtil {
    fun decode(token: String): Claims =
        Jwts.parser().unsecured().build().parseUnsecuredClaims(token).payload
}
