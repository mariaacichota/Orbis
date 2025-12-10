package br.com.orbis.ticketservice.repository;

import br.com.orbis.ticketservice.model.Ticket;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    long countByEventId(Long eventId);
}
