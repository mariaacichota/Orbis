package br.com.orbis.ticketservice.service;

import br.com.orbis.ticketservice.dto.EventMessageDTO;
import br.com.orbis.ticketservice.model.EventInfo;
import br.com.orbis.ticketservice.repository.EventInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class EventSyncService {

    private final EventInfoRepository repo;

    public EventSyncService(EventInfoRepository repo) {
        this.repo = repo;
    }

    public void saveEvent(EventMessageDTO dto) {
        EventInfo event = new EventInfo(dto);
        repo.save(event);
    }

    public void updateEvent(EventMessageDTO dto) {
        EventInfo event = new EventInfo(dto);
        repo.save(event);
    }

    public void deleteEvent(Long id) {
        repo.deleteById(id);
    }
}
