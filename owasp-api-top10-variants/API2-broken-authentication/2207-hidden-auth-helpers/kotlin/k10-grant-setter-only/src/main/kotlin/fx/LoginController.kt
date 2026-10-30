package fx

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(private val authenticationManager: AuthenticationManager) {
    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): String {
        val auth = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        SecurityContextHolder.getContext().authentication = auth
        return "ok"
    }
}
