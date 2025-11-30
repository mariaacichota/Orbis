package br.com.orbis.Orbis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<Speaker> speakers;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Activity> activities;

    private String imageUrl;

    @NotNull(message = "Max tickets is required")
    @Positive(message = "Max tickets must be greater than zero")
    private Integer maxTickets;

    @NotNull(message = "Organizer ID is required")
    private Long organizerId;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be greater than zero")
    private Double baseTicketPrice;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    public boolean hasCapacity() {
        return tickets.size() < maxTickets;
    }

    @ManyToMany
    @JoinTable(
            name = "event_category",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "event_tag",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

}
