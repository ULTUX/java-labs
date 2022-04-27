package pl.edu.pwr.lab7.soap;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.payment.PaymentService;
import pl.edu.pwr.lab7.jpa.person.PersonService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Endpoint
public class EventEndpoint {
    @PersistenceContext
    private EntityManager entityManager;
    private final PersonService personService;
    private final InstallmentService installmentService;
    private final PaymentService paymentService;

    private final EventService eventService;

    public EventEndpoint(PersonService personService, InstallmentService installmentService, PaymentService paymentService, EventService eventService) {
        this.personService = personService;
        this.installmentService = installmentService;
        this.paymentService = paymentService;
        this.eventService = eventService;
    }
}
