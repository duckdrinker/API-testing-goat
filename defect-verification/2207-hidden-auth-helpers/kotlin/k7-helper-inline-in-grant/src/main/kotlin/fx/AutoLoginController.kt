package fx

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AutoLoginController {
    private val key: java.security.PublicKey? = null

    private fun verify(token: String): Claims =
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload

    @PostMapping("/auto-login")
    fun autoLogin(@RequestHeader("X-Token") token: String): String {
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(verify(token).subject, null, emptyList())
        return "ok"
    }
}
