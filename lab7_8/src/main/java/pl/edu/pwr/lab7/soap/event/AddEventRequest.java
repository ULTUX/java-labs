package pl.edu.pwr.lab7.soap.event;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "",propOrder = {
        "name",
        "location",
        "time"
})
@XmlRootElement(name = "addEventRequest", namespace = "http://pwr.edu.pl/soap")
public class AddEventRequest {
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String location;
    @XmlElement(required = true)
    protected Date time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
