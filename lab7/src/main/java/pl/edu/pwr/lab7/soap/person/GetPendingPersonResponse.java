package pl.edu.pwr.lab7.soap.person;

import pl.edu.pwr.lab7.jpa.person.Person;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "personList"
})
@XmlRootElement(name = "getPendingPeopleResponse", namespace = "http://pwr.edu.pl/soap")
public class GetPendingPersonResponse {

    @XmlElement(required = true)
    protected List<Person> personList;

    public List<Person> getPersonist() {
        if (personList == null) personList = new ArrayList<>();
        return personList;
    }
}
