package pl.edu.pwr.lab7.soap.event;

import pl.edu.pwr.lab7.jpa.event.Event;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "eventList"
})
@XmlRootElement(name = "getAllEventResponse", namespace = "http://pwr.edu.pl/soap")
public class GetAllEventResponse {
    @XmlElement(required = true)
    protected List<Event> eventList;

    public List<Event> getEventList() {
        if (eventList == null) eventList = new ArrayList<>();
        return eventList;
    }
}
