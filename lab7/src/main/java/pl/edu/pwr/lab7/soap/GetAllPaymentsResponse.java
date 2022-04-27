package pl.edu.pwr.lab7.soap;

import pl.edu.pwr.lab7.jpa.payment.Payment;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "paymentList",

})
@XmlRootElement(name = "getAllPaymentsResponse", namespace = "http://pwr.edu.pl/soap")
public class GetAllPaymentsResponse {

    @XmlElement(required = true)
    List<Payment> paymentList;


    public List<Payment> getPaymentList() {
        if (paymentList == null) paymentList = new ArrayList<>();
        return paymentList;
    }
}
