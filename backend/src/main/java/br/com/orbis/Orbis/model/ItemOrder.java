package br.com.orbis.Orbis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Entity
public class ItemOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @DecimalMin("0.00")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal price;

    public Ticket getTicket() {
    }

    public double getQuantity() {
    }

    public void setPrice(BigDecimal price) {
    }

    public void setOrder(Order pedido) {
    }

    public BigDecimal getPrice() {
    }
}
