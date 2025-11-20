package br.com.orbis.Orbis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Ticket type is required")
    private TicketType type;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    // User ID from User microservice
    @NotNull(message = "User ID is required")
    private Long userId;

    public Ticket() {}

    public Ticket(TicketType type, Event event, Long userId) {
        this.type = type;
        this.event = event;
        this.userId = userId;
    }

    public double getPrice() {
        return type.calculatePrice(event.getBaseTicketPrice());
    }
}
