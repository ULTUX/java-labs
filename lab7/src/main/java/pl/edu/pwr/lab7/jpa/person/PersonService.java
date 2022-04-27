package pl.edu.pwr.lab7.jpa.person;

import org.springframework.stereotype.Service;
import pl.edu.pwr.lab7.jpa.installment.Installment;
import pl.edu.pwr.lab7.jpa.payment.PaymentRepository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository repository;
    private final PaymentRepository paymentRepository;

    public PersonService(PersonRepository repository, PaymentRepository paymentRepository) {
        this.repository = repository;
        this.paymentRepository = paymentRepository;
    }

    public void addPerson(Person person) {
        repository.save(person);
    }

    public List<Person> getAll() {
        return repository.findAll();
    }

    public Person getById(int id) {
        return repository.getById(id);
    }

    public List<Person> getPendingPeople() {
        return repository.getPending();
    }

    public void importFromFile(String fileName) {
        try {
            var reader = new PersonCSVReader(fileName);
            repository.saveAll(reader.readData());
        } catch (FileNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Provided fileName is not valid or file contents are not consistent.");
        }
    }
}
