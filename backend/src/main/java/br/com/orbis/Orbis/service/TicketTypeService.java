package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.TicketType;
import br.com.orbis.Orbis.repository.TicketTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketTypeService {

    private final TicketTypeRepository repository;

    public TicketTypeService(TicketTypeRepository repository) {
        this.repository = repository;
    }

    public TicketType createTicketType(TicketType ticketType) {
        return repository.save(ticketType);
    }

    public List<TicketType> listTicketTypes() {
        return repository.findAll();
    }

    public TicketType updateTicketType(Long id, TicketType updatedTicketType) {
        Optional<TicketType> existingTicketType = repository.findById(id);
        if (existingTicketType.isEmpty()) {
            throw new IllegalArgumentException("Ticket type not found");
        }
        TicketType ticketTypeToUpdate = existingTicketType.get();
        ticketTypeToUpdate.setDescriptionType(updatedTicketType.getDescriptionType());
        return repository.save(ticketTypeToUpdate);
    }

    public void deleteTicketType(Long id) {
        Optional<TicketType> existingTicketType = repository.findById(id);
        if (existingTicketType.isEmpty()) {
            throw new IllegalArgumentException("Ticket type not found");
        }
        repository.delete(existingTicketType.get());
    }
}

