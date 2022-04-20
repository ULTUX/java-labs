package pl.edu.pwr.lab7.jpa.event;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public void addEvent(Event event) {
        repository.save(event);
    }

    public List<Event> getAll() {
        return repository.findAll();
    }

    public void importFromFile(String fileName){
        try {
            var events = new EventCSVReader(fileName);
            repository.saveAll(events.readData());
        } catch (FileNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Provided fileName is not valid or file contents are not consistent.");
        }
    }

    public Event getById(int id){
        return repository.getById(id);
    }

}
