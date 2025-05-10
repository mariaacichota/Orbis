package br.com.orbis.Orbis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Time is required")
    private LocalTime time;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Organizer is required")
    private Long organizerId;

    private String imageUrl;

    @NotNull(message = "Max tickets is required")
    @Positive(message = "Max tickets must be greater than zero")
    private Integer maxTickets;
}
