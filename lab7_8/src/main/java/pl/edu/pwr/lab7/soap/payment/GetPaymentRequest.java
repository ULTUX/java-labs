package pl.edu.pwr.lab7.soap.payment;


import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "id"
})
@XmlRootElement(name = "getPaymentRequest", namespace = "http://pwr.edu.pl/soap")
public class GetPaymentRequest {

    @XmlElement(required = true)
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}