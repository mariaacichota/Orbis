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
@RequestMapping
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> generateOrder(@Valid @RequestBody List<ItemOrder> itens,
                                             @RequestAttribute("loggedUser") User user) {
        Order pedido = orderService.generateOrder(user, itens);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getPedido(@PathVariable Long id) {
        return orderService.searchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/meus")
    public ResponseEntity<List<Order>> myOrders(@RequestAttribute("loggedUser") User user) {
        return ResponseEntity.ok(orderService.listByUser(user));
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Order> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Order> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}
