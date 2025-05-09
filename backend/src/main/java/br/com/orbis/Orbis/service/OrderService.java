package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.exception.BusinessException;
import br.com.orbis.Orbis.exception.ResourceNotFoundException;
import br.com.orbis.Orbis.model.*;
import br.com.orbis.Orbis.repository.OrderRepository;
import br.com.orbis.Orbis.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    public OrderService(OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    public Order createOrder(User user, List<ItemOrder> items) {
        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemOrder item : items) {
            Ticket ticket = ticketRepository.findById(item.getTicket().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado: " + item.getTicket().getId()));

            if (ticket.getAvailableQuantity() < item.getQuantity()) {
                throw new BusinessException("Ingressos insuficientes para o tipo: "
                        + ticket.getTicketType().getDescriptionType());
            }

            ticket.setAvailableQuantity(ticket.getAvailableQuantity() - item.getQuantity());
            ticketRepository.save(ticket);

            item.setOrder(order);
            item.setPrice(ticket.getPrice());

            total = total.add(ticket.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setItems(items);
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public Order getByIdOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado: " + id));
    }

    public Order confirmOrder(Long id) {
        Order order = getByIdOrThrow(id);
        order.setStatus(OrderStatus.CONFIRMADO);
        return order;
    }

    public Order cancelOrder(Long id) {
        Order order = getByIdOrThrow(id);
        if (order.getStatus() == OrderStatus.CONFIRMADO) {
            order.getItems().forEach(item -> {
                Ticket t = item.getTicket();
                t.setAvailableQuantity(t.getAvailableQuantity() + item.getQuantity());
                ticketRepository.save(t);
            });
        }
        order.setStatus(OrderStatus.CANCELADO);
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}