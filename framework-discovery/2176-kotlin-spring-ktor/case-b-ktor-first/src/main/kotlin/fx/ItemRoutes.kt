package fx

import io.ktor.server.response.*
import io.ktor.server.routing.*

// Mounted from the application module under /api; auth comes from the wrapper in another file
fun Route.itemRoutes() {
    secured {
        get("/items") { call.respondText("items") }
        post("/items") { call.respondText("created") }
    }
    // open route inside the same mounted function (control: must NOT be reported as authenticated)
    get("/items/public") { call.respondText("public items") }
}
