package br.com.orbis.Orbis.repository;

import br.com.orbis.Orbis.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}

