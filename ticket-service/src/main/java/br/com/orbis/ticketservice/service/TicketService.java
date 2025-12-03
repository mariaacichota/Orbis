package br.com.orbis.ticketservice.service.impl;

import br.com.orbis.ticketservice.dto.TicketRequest;
import br.com.orbis.ticketservice.dto.TicketResponse;
import br.com.orbis.ticketservice.model.EventInfo;
import br.com.orbis.ticketservice.model.Ticket;
import br.com.orbis.ticketservice.repository.EventInfoRepository;
import br.com.orbis.ticketservice.repository.TicketRepository;
import br.com.orbis.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventInfoRepository eventInfoRepository;

    @Override
    public TicketResponse sellTicket(TicketRequest request) {

        EventInfo eventInfo = eventInfoRepository.findById(request.getEventId())
                .orElseThrow(() -> new IllegalStateException("Evento não encontrado para o ID: " + request.getEventId()));

        double basePrice = eventInfo.getBaseTicketPrice();
        if (request.getBasePrice() != null && !request.getBasePrice().equals(basePrice)) {
            // add. exceção
        }

        Ticket ticket = new Ticket(
                request.getType(),
                request.getEventId(),
                request.getUserId(),
                basePrice
        );

        Ticket saved = ticketRepository.save(ticket);

        return TicketResponse.builder()
                .id(saved.getId())
                .type(saved.getType())
                .eventId(saved.getEventId())
                .userId(saved.getUserId())
                .basePrice(saved.getBasePrice())
                .finalPrice(saved.getPrice())
                .build();
    }

    @Override
    public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Ticket não encontrado: " + id));

        return TicketResponse.builder()
                .id(ticket.getId())
                .type(ticket.getType())
                .eventId(ticket.getEventId())
                .userId(ticket.getUserId())
                .basePrice(ticket.getBasePrice())
                .finalPrice(ticket.getPrice())
                .build();
    }

    @Override
    public List<TicketResponse> getTicketsByUser(Long userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TicketResponse> getTicketsByEvent(Long eventId) {
        return ticketRepository.findByEventId(eventId).stream()
                .map(this::toResponse)
                .toList();
    }

    private TicketResponse toResponse(Ticket t) {
        return TicketResponse.builder()
                .id(t.getId())
                .type(t.getType())
                .eventId(t.getEventId())
                .userId(t.getUserId())
                .basePrice(t.getBasePrice())
                .finalPrice(t.getPrice())
                .build();
    }
}