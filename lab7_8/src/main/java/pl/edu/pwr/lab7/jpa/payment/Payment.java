package pl.edu.pwr.lab7.jpa.payment;

import pl.edu.pwr.lab7.jpa.event.Event;
import pl.edu.pwr.lab7.jpa.installment.Installment;
import pl.edu.pwr.lab7.jpa.person.Person;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payment", namespace = "http://pwr.edu.pwr/soap", propOrder = {
        "amount",
        "event",
        "id",
        "installment",
        "person",
        "time"
})
@Entity
@Table(name = "payment")
public class Payment {
    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @XmlSchemaType(name = "dateTime")
    @Column(name = "\"time\"", nullable = false)
    private Date time;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "installment_id", nullable = false)
    private Installment installment;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Column(name = "amount", nullable = false)
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}