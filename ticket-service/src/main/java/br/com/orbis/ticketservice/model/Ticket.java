package br.com.orbis.ticketservice.model;

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

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Double basePrice;

    public Ticket() {}

    public Ticket(TicketType type, Long eventId, Long userId, Double basePrice) {
        this.type = type;
        this.eventId = eventId;
        this.userId = userId;
        this.basePrice = basePrice;
    }

    public double getPrice() {
        return type.calculatePrice(basePrice);
    }
}

