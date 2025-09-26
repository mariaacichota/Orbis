package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EventSpecifications {

    // Specification para buscar por título
    public static Specification<Event> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            // Se title for null, retorna null (não aplica filtro)
            if (title == null || title.trim().isEmpty()) {
                return null;
            }
            // Cria um LIKE case-insensitive
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }

    // Specification para buscar por localização
    public static Specification<Event> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null || location.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")),
                    "%" + location.toLowerCase() + "%"
            );
        };
    }

    // Specification para buscar eventos após uma data
    public static Specification<Event> isAfterDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), date);
        };
    }

    // Specification para buscar eventos antes de uma data
    public static Specification<Event> isBeforeDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("date"), date);
        };
    }
}