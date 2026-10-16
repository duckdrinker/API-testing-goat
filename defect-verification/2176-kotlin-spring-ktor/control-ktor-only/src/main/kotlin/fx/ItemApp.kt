package fx

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080) { module() }.start(wait = true)
}

fun Application.module() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(com.auth0.jwt.JWT.require(com.auth0.jwt.algorithms.Algorithm.HMAC256("s")).build())
            validate { JWTPrincipal(it.payload) }
        }
    }
    routing {
        // public, no auth
        get("/health") { call.respondText("UP") }
        // route function declared in ANOTHER file (read later/earlier depending on case)
        route("/api") { itemRoutes() }
    }
}
