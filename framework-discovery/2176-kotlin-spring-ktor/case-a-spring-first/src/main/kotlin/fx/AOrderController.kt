package fx

import io.ktor.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

// Spring controller that also imports Ktor, so both detectors read this file
@RestController
class OrderController {
    @GetMapping("/orders/{id}")
    fun getOrder(@PathVariable id: String): ResponseEntity<String> =
        ResponseEntity.status(HttpStatusCode.OK.value).body("order $id")
}
