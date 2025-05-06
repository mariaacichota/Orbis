package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.*;
import br.com.orbis.Orbis.repository.OrderRepository;
import br.com.orbis.Orbis.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final TicketRepository ticketRepo;

    public OrderService(OrderRepository orderRepo, TicketRepository ticketRepo) {
        this.orderRepo = orderRepo;
        this.ticketRepo = ticketRepo;
    }

    @Transactional
    public Order generateOrder(User user, List<ItemOrder> itens) {
        Order pedido = new Order();
        pedido.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        for (ItemOrder item : itens) {
            // valida disponibilidade
            Ticket ticket = ticketRepo.findById(item.getTicket().getId())
                    .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
            if (ticket.getAvailableQuantity() < item.getQuantity()) {
                throw new RuntimeException("Ingressos insuficientes para o tipo “" +
                        ticket.getTicketType().getDescriptionType() + "”");
            }
            // decrementa estoque
            ticket.setAvailableQuantity((int) (ticket.getAvailableQuantity() - item.getQuantity()));
            ticketRepo.save(ticket);

            // ajusta preço e vincula item
            item.setPrice(ticket.getPrice());
            item.setOrder(pedido);
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        pedido.setItems(itens);
        pedido.setTotalAmount(total);
        // status fica PENDENTE por padrão
        return orderRepo.save(pedido);
    }

    @Transactional
    public Order confirmOrder(Long pedidoId) {
        Order pedido = orderRepo.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(OrderStatus.CONFIRMADO);
        return pedido;
    }

    @Transactional
    public Order cancelOrder(Long pedidoId) {
        //TODO
        return null;
    }

    public List<Order> listByUser(User user) {
        return orderRepo.findByUser(user);
    }

    public Optional<Order> searchById(Long id) {
        return orderRepo.findById(id);
    }
}