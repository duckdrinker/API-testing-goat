package fx

import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class AutoLoginController {
    private val key: java.security.PublicKey? = null

    @PostMapping("/auto-login")
    fun autoLogin(@RequestHeader("X-Token") token: String): String {
        val claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
        SecurityContextHolder.getContext().setAuthentication(
            UsernamePasswordAuthenticationToken(claims.subject, null, emptyList()))
        return "ok"
    }
}
