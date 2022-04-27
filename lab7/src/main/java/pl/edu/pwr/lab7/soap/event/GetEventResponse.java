package pl.edu.pwr.lab7.soap.event;

import pl.edu.pwr.lab7.jpa.event.Event;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "event"
})
@XmlRootElement(name = "getEventResponse", namespace = "http://pwr.edu.pl/soap")
public class GetEventResponse {
    @XmlElement(required = true)
    protected Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
