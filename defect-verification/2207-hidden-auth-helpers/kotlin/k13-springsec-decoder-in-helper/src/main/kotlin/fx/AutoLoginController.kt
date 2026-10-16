package fx

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class AutoLoginController {
    private val decoder: JwtDecoder = NimbusJwtDecoder.withJwkSetUri("https://idp.example/jwks").build()

    private fun verify(token: String): Jwt = decoder.decode(token)

    @PostMapping("/auto-login")
    fun autoLogin(@RequestHeader("X-Token") token: String): String {
        val jwt = verify(token)
        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(jwt.subject, null, emptyList())
        return "ok"
    }
}
