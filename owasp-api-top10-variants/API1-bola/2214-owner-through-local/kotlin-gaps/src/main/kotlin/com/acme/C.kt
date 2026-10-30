package com.acme
import java.security.Principal
import org.springframework.web.bind.annotation.*

@RestController
class C(private val repo: OrderRepository) {
    @GetMapping("/v1/{id}")
    fun v1(@PathVariable id: Long, principal: Principal): Order {
        val order = repo.findById(id).orElseThrow()
        if (order.owner != principal.name) throw IllegalStateException("x")
        return order
    }
    @GetMapping("/v2/{id}")
    fun v2(@PathVariable id: Long, principal: Principal): Order {
        val owner = principal.name
        val order = repo.findById(id).orElseThrow()
        if (order.owner != owner) throw IllegalStateException("x")
        return order
    }
    @GetMapping("/v3/{id}")
    fun v3(@PathVariable id: Long, principal: Principal): Order? {
        val order = repo.findById(id).orElse(null) // KOTLIN-GAP-1 (6.20.2: not reported, 6.22.0: reported)
        if (order == null || order.owner != principal.name) return null
        return order
    }
    @GetMapping("/v4/{id}")
    fun v4(@PathVariable id: Long, @AuthenticationPrincipal user: AppUser): Order { // KOTLIN-GAP-2 (reported in 6.20.2 and 6.22.0)
        val order = repo.findById(id).orElseThrow()
        if (order.owner != user.username) throw IllegalStateException("x")
        return order
    }
    @GetMapping("/v5/{id}")
    fun v5(@PathVariable id: Long, principal: Principal): Order {
        val order = repo.findById(id).get()
        if (order.owner != principal.name) { throw IllegalStateException("x") }
        return order
    }
}