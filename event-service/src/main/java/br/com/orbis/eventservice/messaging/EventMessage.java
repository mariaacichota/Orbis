package br.com.orbis.eventservice.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage implements Serializable {
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

