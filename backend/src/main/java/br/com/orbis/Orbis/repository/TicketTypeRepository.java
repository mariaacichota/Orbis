package br.com.orbis.Orbis.repository;

import br.com.orbis.Orbis.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
}
