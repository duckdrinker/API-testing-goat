package com.acme
class Order(val id: Long, val owner: String)
class AppUser(val username: String)
interface OrderRepository : org.springframework.data.jpa.repository.JpaRepository<Order, Long>
