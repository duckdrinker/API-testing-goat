package fx

import io.ktor.server.auth.*
import io.ktor.server.routing.*

// Auth wrapper defined in a separate file
fun Route.secured(build: Route.() -> Unit) {
    authenticate("auth-jwt") { build() }
}
