package pl.edu.pwr.lab7.soap.event;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "id"
})
@XmlRootElement(name = "getEventRequest", namespace = "http://pwr.edu.pl/soap")
public class GetEventRequest {
    @XmlElement(required = true)
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
