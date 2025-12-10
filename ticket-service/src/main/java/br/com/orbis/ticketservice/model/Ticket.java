package br.com.orbis.ticketservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Document(collection = "tickets")
@Data
public class Ticket {

    @Id
    private String id;

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