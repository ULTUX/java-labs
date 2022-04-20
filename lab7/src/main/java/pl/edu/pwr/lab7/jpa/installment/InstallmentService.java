package pl.edu.pwr.lab7.jpa.installment;

import org.springframework.stereotype.Service;
import pl.edu.pwr.lab7.jpa.event.EventService;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class InstallmentService {

    private final InstallmentRepository repository;
    private final EventService eventService;

    public InstallmentService(InstallmentRepository repository, EventService eventService) {
        this.repository = repository;
        this.eventService = eventService;
    }

    public void addInstallment(Installment installment){
        repository.save(installment);
    }

    public List<Installment> getAll() {
        return repository.findAll();
    }

    public void importFromFile(String fileName){
        try {
            var installments = new InstallmentCSVReader(fileName, eventService);
            repository.saveAll(installments.readData());
        } catch (FileNotFoundException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided fileName is not valid or file contents are not consistent.");
        }
    }

    public Installment getById(int id) {
        return repository.getById(id);
    }

}
