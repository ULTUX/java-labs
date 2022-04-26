package pl.edu.pwr.lab7;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.person.Person;
import pl.edu.pwr.lab7.jpa.person.PersonRepository;
import pl.edu.pwr.lab7.soap.GetPerson;
import pl.edu.pwr.lab7.soap.GetResponse;

@Endpoint
public class PersonEndpoint {

    private final PersonRepository repository;

    @Autowired
    public PersonEndpoint(PersonRepository repository) {
        this.repository = repository;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPerson")
    @ResponsePayload
    public GetResponse getPersonById(@RequestPayload GetPerson getPerson) {
        var resp = new GetResponse();
        resp.setPerson(new Person());
        return resp;
    }

}
