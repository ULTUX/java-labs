package pl.edu.pwr.lab7.soap;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.payment.Payment;
import pl.edu.pwr.lab7.jpa.payment.PaymentService;
import pl.edu.pwr.lab7.jpa.person.PersonService;
import pl.edu.pwr.lab7.soap.payment.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Endpoint
public class PaymentEndpoint {

    @PersistenceContext
    private EntityManager entityManager;
    private final PersonService personService;
    private final InstallmentService installmentService;
    private final PaymentService paymentService;

    private final EventService eventService;

    public PaymentEndpoint(PersonService personService, InstallmentService installmentService, PaymentService paymentService, EventService eventService) {
        this.personService = personService;
        this.installmentService = installmentService;
        this.paymentService = paymentService;
        this.eventService = eventService;
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

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getPaymentRequest")
    @ResponsePayload
    public GetPaymentResponse getPayment(@RequestPayload GetPaymentRequest req) {
        var payment = paymentService.getById(req.getId());
        payment = (Payment) Hibernate.unproxy(payment);
        var resp = new GetPaymentResponse();
        resp.setPayment(payment);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "addPaymentRequest")
    @ResponsePayload
    public AddPaymentResponse addPayment(@RequestPayload AddPaymentRequest req) {
        var payment = new Payment();
        payment.setTime(req.getTime());
        payment.setInstallment(installmentService.getById(req.getInstallmentId()));
        payment.setEvent(eventService.getById(req.getEventId()));
        payment.setPerson(personService.getById(req.getPersonId()));
        payment.setAmount(req.getAmount());
        paymentService.addPayment(payment);
        return new AddPaymentResponse();
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "deletePaymentRequest")
    @ResponsePayload
    public DeletePaymentResponse deletePayment(@RequestPayload DeletePaymentRequest req) {
        paymentService.deleteById(req.getId());
        return new DeletePaymentResponse();
    }
}
