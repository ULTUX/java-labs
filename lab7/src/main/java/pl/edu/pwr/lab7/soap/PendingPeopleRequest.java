package pl.edu.pwr.lab7.soap;


import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "installmentId"
})
@XmlRootElement(name = "getPendingPeopleRequest", namespace = "http://pwr.edu.pl/soap")
public class PendingPeopleRequest {

    @XmlElement(required = true)
    protected int installmentId;

    public int getInstallmentId() {
        return installmentId;
    }

    public void setInstallment(int installment) {
        this.installmentId = installment;
    }
}
