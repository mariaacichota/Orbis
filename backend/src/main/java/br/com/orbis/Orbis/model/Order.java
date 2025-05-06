package br.com.orbis.Orbis.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDENTE;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrder> items = new ArrayList<>();

    public void setUser(User user) {
    }

    public void setItems(List<ItemOrder> itens) {
    }

    public void setTotalAmount(BigDecimal total) {
    }

    public void setStatus(OrderStatus orderStatus) {
    }

    public boolean getStatus() {
    }

    public Iterable<Object> getItems() {
        return null;
    }
}
