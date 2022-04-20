package pl.edu.pwr.lab7.jpa.payment;

import org.springframework.stereotype.Service;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.person.PersonService;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class PaymentService {
    private final PersonService personService;
    private final InstallmentService installmentService;
    private final EventService eventService;

    private final PaymentRepository repository;

    public PaymentService(PersonService personService, InstallmentService installmentService, EventService eventService, PaymentRepository repository) {
        this.personService = personService;
        this.installmentService = installmentService;
        this.eventService = eventService;
        this.repository = repository;
    }

    public void addPayment(Payment payment) {
        repository.save(payment);
    }

    public List<Payment> getAll() {
        return repository.findAll();
    }

    public void importFromFile(String fileName) {
        try {
            var reader = new PaymentCSVReader(fileName, personService, installmentService, eventService);
            repository.saveAll(reader.readData());
        } catch (FileNotFoundException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided fileName is not valid or file contents are not consistent.");
        }
    }

}
