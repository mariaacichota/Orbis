package br.com.orbis.eventservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "activity name required.")
    private String name;
    @NotBlank(message = "activity description required.")
    private String description;

}

