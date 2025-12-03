package br.com.orbis.ticketservice.model;

import br.com.orbis.ticketservice.dto.EventMessageDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventInfo {

    @Id
    private Long id;

    private String title;
    private LocalDate date;
    private LocalTime time;
    private Double baseTicketPrice;

    public EventInfo(EventMessageDTO dto) {
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.date = dto.getDate();
        this.time = dto.getTime();
        this.baseTicketPrice = dto.getBaseTicketPrice();
    }
}
