package br.com.orbis.Orbis.repository;

import br.com.orbis.Orbis.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByEventId(Long eventId);
}
