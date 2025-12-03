package br.com.orbis.ticketservice.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventMessageDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String location;
    private Integer maxTickets;
    private Long organizerId;
    private Double baseTicketPrice;
    private String imageUrl;
}
