package pl.edu.pwr.lab7.soap.installment;

import pl.edu.pwr.lab7.jpa.installment.Installment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "installmentList"
})
@XmlRootElement(name = "getAllInstallmentResponse", namespace = "http://pwr.edu.pl/soap")
public class GetAllInstallmentResponse {
    List<Installment> installmentList;

    public List<Installment> getInstallmentList() {
        if (installmentList == null) installmentList = new ArrayList<>();
        return installmentList;
    }
}
