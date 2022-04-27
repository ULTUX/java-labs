package pl.edu.pwr.lab7.soap;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.payment.PaymentService;
import pl.edu.pwr.lab7.jpa.person.Person;
import pl.edu.pwr.lab7.jpa.person.PersonService;
import pl.edu.pwr.lab7.soap.person.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Endpoint
public class PersonEndpoint {

    @PersistenceContext
    private EntityManager entityManager;
    private final PersonService personService;
    private final InstallmentService installmentService;
    private final PaymentService paymentService;

    @Autowired
    public PersonEndpoint(PersonService personService, InstallmentService installmentService, PaymentService paymentService) {
        this.personService = personService;
        this.installmentService = installmentService;
        this.paymentService = paymentService;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPersonRequest")
    @ResponsePayload
    public GetPersonRequest getPersonById(@RequestPayload GetPersonResponse getPerson) {
        var resp = new GetPersonRequest();
        var found = personService.getById(getPerson.getId());
        found = (Person) Hibernate.unproxy(found);
        resp.setPerson(found);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getAllPersonRequest")
    @ResponsePayload
    public GetAllPersonResponse getAllPerson(@RequestPayload GetAllPersonRequest getPerson) {
        var resp = new GetAllPersonResponse();
        var found = personService.getAll();
        Hibernate.unproxy(found);
        resp.getPerson().addAll(found);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPendingPeopleRequest")
    @ResponsePayload
    public GetPendingPersonResponse getPendingPerson(@RequestPayload GetPendingPeopleRequest getPerson) {
        var pending = personService.getPendingPeople();
        Hibernate.unproxy(pending);
        var resp = new GetPendingPersonResponse();
        resp.getPersonist().addAll(pending);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "addPersonRequest")
    @ResponsePayload
    public SavePersonResponse addPerson(@RequestPayload SavePersonRequest req) {
        var person = new Person();
        person.setFirstName(req.getFirstName());
        person.setLastName(req.getLastName());
        personService.addPerson(person);
        return new SavePersonResponse();
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "deletePersonRequest")
    @ResponsePayload
    public DeletePersonResponse deletePerson(@RequestPayload DeletePersonRequest req) {
        var id = req.getId();
        personService.deleteById(id);
        return new DeletePersonResponse();
    }

}
