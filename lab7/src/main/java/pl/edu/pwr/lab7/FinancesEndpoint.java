package pl.edu.pwr.lab7;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.person.Person;
import pl.edu.pwr.lab7.jpa.person.PersonRepository;
import pl.edu.pwr.lab7.jpa.person.PersonService;
import pl.edu.pwr.lab7.soap.GetPersonRequest;
import pl.edu.pwr.lab7.soap.GetPersonResponse;
import pl.edu.pwr.lab7.soap.ObjectFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@Transactional
@Endpoint
public class FinancesEndpoint {

    @PersistenceContext
    private EntityManager entityManager;
    private final PersonService personService;

    @Autowired
    public FinancesEndpoint(PersonService personService) {
        this.personService = personService;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPersonRequest")
    @ResponsePayload
    public GetPersonResponse getPersonById(@RequestPayload GetPersonRequest getPerson) {
        var resp = new GetPersonResponse();
        var found = personService.getById(getPerson.getId());
        found = (Person) Hibernate.unproxy(found);
        resp.setPerson(found);
        return resp;
    }

}
