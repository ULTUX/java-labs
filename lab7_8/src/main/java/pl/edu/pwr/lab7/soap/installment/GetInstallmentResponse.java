package pl.edu.pwr.lab7.soap.installment;

import pl.edu.pwr.lab7.jpa.installment.Installment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "installment"
})
@XmlRootElement(name = "getInstallmentResponse", namespace = "http://pwr.edu.pl/soap")
public class GetInstallmentResponse {
    protected Installment installment;

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }
}
