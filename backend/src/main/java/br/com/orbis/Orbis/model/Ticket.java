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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required")
    private User user;

    public Ticket() {}

    public Ticket(TicketType type, Event event, User user) {
        this.type = type;
        this.event = event;
        this.user = user;
    }

    public double getPrice() {
        return type.calculatePrice(event.getBaseTicketPrice());
    }
}
