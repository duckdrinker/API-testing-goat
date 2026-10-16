package fx

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class AutoLoginController {
    @PostMapping("/auto-login")
    fun autoLogin(@RequestHeader("X-Token") token: String): String {
        val decoded = JWT.require(Algorithm.HMAC256("secret")).build().verify(token)
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(decoded.subject, null, emptyList())
        return "ok"
    }
}
