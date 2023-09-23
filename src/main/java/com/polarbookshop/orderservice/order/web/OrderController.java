package com.polarbookshop.orderservice.order.web;

import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public Flux<Order> getAllOrders(@AuthenticationPrincipal Jwt jwt){
        log.info("Fetching all orders");
        return orderService.getAllOrders(jwt.getSubject());
    }
    @PostMapping
    public Mono<Order> submitOrder(@RequestBody OrderRequest orderRequest){
        log.info("Order for {} copies of the book with ISBN {}", orderRequest.quantity(), orderRequest.isbn());
        return orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
    }
}
