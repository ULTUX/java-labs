package pl.edu.pwr.lab7.soap.payment;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "time",
        "installmentId",
        "eventId",
        "personId",
        "amount"
})
@XmlRootElement(name = "addPaymentRequest", namespace = "http://pwr.edu.pl/soap")
public class AddPaymentRequest {
    @XmlElement(required = true)
    protected Date time;
    @XmlElement(required = true)
    protected int installmentId;
    @XmlElement(required = true)
    protected int eventId;
    @XmlElement(required = true)
    protected int personId;
    @XmlElement(required = true)
    protected double amount;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getInstallmentId() {
        return installmentId;
    }

    public void setInstallmentId(int installmentId) {
        this.installmentId = installmentId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
