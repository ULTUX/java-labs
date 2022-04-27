package pl.edu.pwr.lab7.soap.payment;

import pl.edu.pwr.lab7.jpa.payment.Payment;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "payment"
})
@XmlRootElement(name = "getPaymentResponse", namespace = "http://pwr.edu.pl/soap")
public class GetPaymentResponse {

    @XmlElement(required = true)
    protected Payment payment;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
