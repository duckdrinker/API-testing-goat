package com.acme

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Entity
class Order(@Id var id: Long = 0, var owner: String = "")

interface OrderRepo : JpaRepository<Order, Long>

/**
 * #2214 - owner bound through a local variable (Spring Data, Kotlin).
 * NOT REPORTED cases must stay silent; REPORTED cases must fire
 * kotlin.user_controlled_primary_key.
 * The Kotlin lookup takes only the key, so the owner check is the comparison
 * after the fetch (place B of the ticket).
 */
@RestController
class OrderController(private val repo: OrderRepo) {

    // CTRL-1 REPORTED: unbounded lookup, baseline positive.
    @GetMapping("/ctrl1/{id}")
    fun ctrl1(@PathVariable id: Long): Any? = repo.findById(id).orElse(null)

    // CTRL-2 NOT REPORTED: comparison after the lookup, caller written inline.
    @GetMapping("/ctrl2/{id}")
    fun ctrl2(@PathVariable id: Long, principal: Principal): Any? {
        val order = repo.findById(id).orElse(null)
        if (order.owner != principal.name) throw IllegalStateException("forbidden")
        return order
    }

    // B1 NOT REPORTED: comparison after the lookup, caller held in a local.
    @GetMapping("/b1/{id}")
    fun b1(@PathVariable id: Long, principal: Principal): Any? {
        val owner = principal.name
        val order = repo.findById(id).orElse(null)
        if (order.owner != owner) throw IllegalStateException("forbidden")
        return order
    }

    // B2 REPORTED: twin of B1, the local holds a request value.
    @GetMapping("/b2/{id}")
    fun b2(@PathVariable id: Long, request: HttpServletRequest): Any? {
        val owner = request.getParameter("owner")
        val order = repo.findById(id).orElse(null)
        if (order.owner != owner) throw IllegalStateException("forbidden")
        return order
    }

    // C2 REPORTED: local first takes the caller, then is rebound to a request value.
    @GetMapping("/c2/{id}")
    fun c2(@PathVariable id: Long, principal: Principal, request: HttpServletRequest): Any? {
        var owner = principal.name
        owner = request.getParameter("owner")
        val order = repo.findById(id).orElse(null)
        if (order.owner != owner) throw IllegalStateException("forbidden")
        return order
    }

    // D1 REPORTED: loop variable, not assigned once from the caller.
    @GetMapping("/d1/{id}")
    fun d1(@PathVariable id: Long, request: HttpServletRequest): Any? {
        val order = repo.findById(id).orElse(null)
        for (owner in request.getParameterValues("owners")) {
            if (order.owner == owner) return order
        }
        throw IllegalStateException("forbidden")
    }
}
