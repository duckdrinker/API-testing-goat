package com.acme;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndOwner(Long id, String owner);
}
