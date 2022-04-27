package pl.edu.pwr.lab7.soap;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.person.Person;
import pl.edu.pwr.lab7.jpa.person.PersonService;
import pl.edu.pwr.lab7.soap.person.*;


@Transactional
@Endpoint
public class PersonEndpoint {

    private final PersonService personService;

    @Autowired
    public PersonEndpoint(PersonService personService) {
        this.personService = personService;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPersonRequest")
    @ResponsePayload
    public GetPersonRequest get(@RequestPayload GetPersonResponse getPerson) {
        var resp = new GetPersonRequest();
        var found = personService.getById(getPerson.getId());
        found = (Person) Hibernate.unproxy(found);
        resp.setPerson(found);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getAllPersonRequest")
    @ResponsePayload
    public GetAllPersonResponse getAll(@RequestPayload GetAllPersonRequest getPerson) {
        var resp = new GetAllPersonResponse();
        var found = personService.getAll();
        Hibernate.unproxy(found);
        resp.getPerson().addAll(found);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPendingPeopleRequest")
    @ResponsePayload
    public GetPendingPersonResponse getPending(@RequestPayload GetPendingPeopleRequest getPerson) {
        var pending = personService.getPendingPeople();
        Hibernate.unproxy(pending);
        var resp = new GetPendingPersonResponse();
        resp.getPersonist().addAll(pending);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "addPersonRequest")
    @ResponsePayload
    public SavePersonResponse add(@RequestPayload SavePersonRequest req) {
        var person = new Person();
        person.setFirstName(req.getFirstName());
        person.setLastName(req.getLastName());
        personService.addPerson(person);
        return new SavePersonResponse();
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "deletePersonRequest")
    @ResponsePayload
    public DeletePersonResponse delete(@RequestPayload DeletePersonRequest req) {
        var id = req.getId();
        personService.deleteById(id);
        return new DeletePersonResponse();
    }

}
