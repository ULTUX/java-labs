package pl.edu.pwr.lab7.jpa.installment;

import pl.edu.pwr.lab7.jpa.event.Event;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "installment", namespace = "http://pwr.edu.pl/soap", propOrder = {
        "amount",
        "event",
        "id",
        "installmentNum",
        "time"
})
@Entity
@Table(name = "installment")
public class Installment {

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Column(name = "installment_num", nullable = false)
    private Integer installmentNum;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @XmlSchemaType(name = "dateTime")
    @Column(name = "\"time\"", nullable = false)
    private Date time;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Column(name = "amount", nullable = false)
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getInstallmentNum() {
        return installmentNum;
    }

    public void setInstallmentNum(Integer installmentNum) {
        this.installmentNum = installmentNum;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(installmentNum);
    }
}