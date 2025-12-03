package br.com.orbis.ticketservice.repository;

import br.com.orbis.ticketservice.model.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
}
