package br.com.orbis.ticketservice.dto;

import br.com.orbis.ticketservice.model.TicketType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponse {

    private Long id;
    private TicketType type;
    private Long eventId;
    private Long userId;
    private Double basePrice;
    private Double finalPrice;  // basePrice * multiplier
}
