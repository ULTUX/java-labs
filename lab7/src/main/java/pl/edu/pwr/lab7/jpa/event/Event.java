package pl.edu.pwr.lab7.jpa.event;


import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "event", namespace = "http://pwr.edu.pl/soap", propOrder = {
        "id",
        "location",
        "name",
        "time"
})
@Entity
@Table(name = "event")
public class Event {

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Column(name = "location", nullable = false, length = 50)
    private String location;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @XmlSchemaType(name = "dateTime")
    @Column(name = "\"time\"", nullable = false)
    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}