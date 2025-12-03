package br.com.orbis.ticketservice.dto;

import br.com.orbis.ticketservice.model.TicketType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketRequest {
    private TicketType type;
    private Long eventId;
    private Long userId;
    private Double basePrice;
}