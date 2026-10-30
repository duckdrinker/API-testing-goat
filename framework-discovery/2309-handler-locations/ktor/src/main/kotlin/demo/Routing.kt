package demo

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/inline") {
            call.respondText("inline")
        }
        get("/delegated") {
            handleDelegated(call)
        }
    }
}
