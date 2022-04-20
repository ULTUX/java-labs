package pl.edu.pwr.lab7.jpa.person;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
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
