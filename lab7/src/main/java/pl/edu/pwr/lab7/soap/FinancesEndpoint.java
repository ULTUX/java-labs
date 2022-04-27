package pl.edu.pwr.lab7.soap;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.payment.Payment;
import pl.edu.pwr.lab7.jpa.payment.PaymentService;
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
    private final PaymentService paymentService;

    @Autowired
    public FinancesEndpoint(PersonService personService, InstallmentService installmentService, PaymentService paymentService) {
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
    public GetPendingPeopleResponse getPendingPeople(@RequestPayload GetPendingPeopleRequest getPerson) {
        var pending = personService.getPendingPeople();
        Hibernate.unproxy(pending);
        var resp = new GetPendingPeopleResponse();
        resp.getPersonist().addAll(pending);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getAllPaymentsRequest")
    @ResponsePayload
    public GetAllPaymentsResponse getAllPayments(@RequestPayload GetAllPaymentsRequest getPayments) {
        var payments= paymentService.getAll();
        Hibernate.unproxy(payments);
        var resp = new GetAllPaymentsResponse();
        resp.getPaymentList().addAll(payments);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "addPersonRequest")
    @ResponsePayload
    public AddPersonResponse addPerson(@RequestPayload AddPersonRequest req) {
        var person = new Person();
        person.setFirstName(req.firstName);
        person.setLastName(req.lastName);
        personService.addPerson(person);
        return new AddPersonResponse();
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPaymentRequest")
    @ResponsePayload
    public GetPaymentResponse getPayment(@RequestPayload GetPaymentRequest req) {
        var payment = paymentService.getById(req.id);
        payment = (Payment) Hibernate.unproxy(payment);
        var resp = new GetPaymentResponse();
        resp.setPayment(payment);
        return resp;
    }
}
