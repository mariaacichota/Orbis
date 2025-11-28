package br.com.orbis.ticketservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EventClient {

    @Value("${event.service.url}")
    private String eventServiceUrl;

    private final RestTemplate restTemplate;

    public EventClient() {
        this.restTemplate = new RestTemplate();
    }

    public EventResponse getEventById(Long eventId) {
        String url = eventServiceUrl + "/events/" + eventId;
        return restTemplate.getForObject(url, EventResponse.class);
    }

    public static class EventResponse {
        private Long id;
        private String title;
        private Integer maxTickets;
        private Double baseTicketPrice;

        public EventResponse() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getMaxTickets() {
            return maxTickets;
        }

        public void setMaxTickets(Integer maxTickets) {
            this.maxTickets = maxTickets;
        }

        public Double getBaseTicketPrice() {
            return baseTicketPrice;
        }

        public void setBaseTicketPrice(Double baseTicketPrice) {
            this.baseTicketPrice = baseTicketPrice;
        }
    }
}

