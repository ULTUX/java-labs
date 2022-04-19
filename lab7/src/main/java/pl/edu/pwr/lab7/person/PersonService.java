package pl.edu.pwr.lab7.person;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }
}
