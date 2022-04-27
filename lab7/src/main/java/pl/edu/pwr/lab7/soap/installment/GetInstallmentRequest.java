package pl.edu.pwr.lab7.soap.installment;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "",propOrder = {
        "id"
})
@XmlRootElement(name = "getInstallmentRequest", namespace = "http://pwr.edu.pl/soap")
public class GetInstallmentRequest {
    @XmlElement(required = true)
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
