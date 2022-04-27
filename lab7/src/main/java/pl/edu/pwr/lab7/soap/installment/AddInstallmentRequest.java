package pl.edu.pwr.lab7.soap.installment;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "eventId",
        "installmentNum",
        "time",
        "amount"
})
@XmlRootElement(name = "addInstallmentRequest", namespace = "http://pwr.edu.pl/soap")
public class AddInstallmentRequest {

    @XmlElement(required = true)
    protected int eventId;
    @XmlElement(required = true)
    protected int installmentNum;
    @XmlElement(required = true)
    protected Date time;
    @XmlElement(required = true)
    protected double amount;


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getInstallmentNum() {
        return installmentNum;
    }

    public void setInstallmentNum(int installmentNum) {
        this.installmentNum = installmentNum;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
