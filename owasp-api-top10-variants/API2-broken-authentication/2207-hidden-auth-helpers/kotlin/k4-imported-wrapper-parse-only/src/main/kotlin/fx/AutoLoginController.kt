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
    @PostMapping("/auto-login")
    fun autoLogin(@RequestHeader("X-Token") token: String): String {
        val claims = JwtUtil.decode(token)
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(claims.subject, null, emptyList())
        return "ok"
    }
}
