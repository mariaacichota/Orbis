package br.com.orbis.ticketservice.repository;

import br.com.orbis.ticketservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByEventId(Long eventId);
}

