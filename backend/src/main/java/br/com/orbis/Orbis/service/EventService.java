package br.com.orbis.Orbis.service;

import br.com.orbis.Orbis.model.Event;
import br.com.orbis.Orbis.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EventService {

    private final EventRepository repository;
    private final String UPLOAD_DIR = "uploads/";

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event createEvent(Event event, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imagePath = UPLOAD_DIR + image.getOriginalFilename();
            Files.copy(image.getInputStream(), Paths.get(imagePath));
            event.setImageUrl(imagePath);
        }
        return repository.save(event);
    }
}

