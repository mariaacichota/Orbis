package br.com.orbis.Orbis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_event", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "id_ticket_types", nullable = false)
    private TicketType ticketType;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price must be greater than or equal to zero")
    private BigDecimal price;

    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Available quantity must be zero or greater")
    private Integer availableQuantity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public TicketType getTicketType() { return ticketType; }
    public void setTicketType(TicketType ticketType) { this.ticketType = ticketType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
}
