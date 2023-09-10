package com.polarbookshop.orderservice.order.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order,Long> {
    /**
     * Custom method to query all orders created by a given user
     * @param userId
     * @return
     */
    Flux<Order> findAllByCreatedBy(String userId);
}
