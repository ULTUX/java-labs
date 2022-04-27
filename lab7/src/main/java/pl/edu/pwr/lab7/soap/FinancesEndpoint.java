package pl.edu.pwr.lab7.soap;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.person.Person;
import pl.edu.pwr.lab7.jpa.person.PersonService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Endpoint
public class FinancesEndpoint {

    @PersistenceContext
    private EntityManager entityManager;
    private final PersonService personService;
    private final InstallmentService installmentService;

    @Autowired
    public FinancesEndpoint(PersonService personService, InstallmentService installmentService) {
        this.personService = personService;
        this.installmentService = installmentService;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPersonRequest")
    @ResponsePayload
    public PersonRequest getPersonById(@RequestPayload PersonResponse getPerson) {
        var resp = new PersonRequest();
        var found = personService.getById(getPerson.getId());
        found = (Person) Hibernate.unproxy(found);
        resp.setPerson(found);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getAllPersonRequest")
    @ResponsePayload
    public AllPersonResponse getAllPerson(@RequestPayload AllPersonRequest getPerson) {
        var resp = new AllPersonResponse();
        var found = personService.getAll();
        Hibernate.unproxy(found);
        resp.getPerson().addAll(found);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPendingPeopleRequest")
    @ResponsePayload
    public PendingPeopleResponse getPendingPeople(@RequestPayload PendingPeopleRequest getPerson) {
        var installment = installmentService.getById(getPerson.getInstallmentId());
        var pendingPeople = personService.getPendingPeople(installment);
        Hibernate.unproxy(pendingPeople);
        var resp = new PendingPeopleResponse();
        resp.getPersonist().addAll(pendingPeople);
        return resp;
    }

}
