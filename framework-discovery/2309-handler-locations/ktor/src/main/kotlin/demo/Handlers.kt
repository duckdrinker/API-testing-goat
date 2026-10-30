package demo

import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun handleDelegated(call: ApplicationCall) {
    call.respondText("delegated")
}
