package br.com.orbis.Orbis.controller;

import br.com.orbis.Orbis.model.ItemOrder;
import br.com.orbis.Orbis.model.Order;
import br.com.orbis.Orbis.model.User;
import br.com.orbis.Orbis.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody List<ItemOrder> items,
                                             @RequestAttribute("loggedUser") User user) {
        Order order = orderService.createOrder(user, items);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.getByIdOrThrow(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(@RequestAttribute("loggedUser") User user) {
        List<Order> list = orderService.findByUserId(user.getId());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Order> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}