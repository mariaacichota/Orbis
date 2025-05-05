package br.com.orbis.Orbis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description is required")
    @Size(max = 50, message = "Description must be at most 50 characters")
    @Column(unique = true, nullable = false)
    private String descriptionType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescriptionType() { return descriptionType; }
    public void setDescriptionType(String descriptionType) { this.descriptionType = descriptionType; }
}