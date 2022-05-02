package pl.edu.pwr.lab7.soap.person;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "firstName",
        "lastName"
})
@XmlRootElement(name = "addPersonRequest", namespace = "http://pwr.edu.pl/soap")
public class SavePersonRequest {

    @XmlElement(required = true)
    protected String firstName;

    @XmlElement(required = true)
    protected String lastName;

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
}
