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

    private fun parse(token: String): Claims =
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload

    private fun subjectFrom(token: String): String = parse(token).subject

    @PostMapping("/auto-login")
    fun autoLogin(@RequestHeader("X-Token") token: String): String {
        val subject = subjectFrom(token)
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(subject, null, emptyList())
        return "ok"
    }
}
