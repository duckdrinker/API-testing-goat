package fx

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

// Spring-only control: no Ktor in this project
@RestController
class OrderController {
    @GetMapping("/orders/{id}")
    fun getOrder(@PathVariable id: String): ResponseEntity<String> =
        ResponseEntity.status(200).body("order $id")
}
