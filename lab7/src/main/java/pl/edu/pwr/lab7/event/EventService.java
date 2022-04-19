package pl.edu.pwr.lab7.event;

import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }
}
