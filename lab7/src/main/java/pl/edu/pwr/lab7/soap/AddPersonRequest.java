package pl.edu.pwr.lab7.soap;

import pl.edu.pwr.lab7.jpa.person.Person;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "firstName",
        "lastName"
})
@XmlRootElement(name = "addPersonRequest", namespace = "http://pwr.edu.pl/soap")
public class AddPersonRequest {

    @XmlElement(required = true)
    protected String firstName;

    @XmlElement(required = true)
    protected String lastName;

}
