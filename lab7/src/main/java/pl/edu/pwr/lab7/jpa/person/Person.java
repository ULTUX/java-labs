package pl.edu.pwr.lab7.jpa.person;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "person", namespace = "http://pwr.edu.pl/soap", propOrder = {
        "firstName",
        "id",
        "lastName"
})
@Entity
@Table(name = "person")
public class Person {
    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @XmlElement(namespace = "http://pwr.edu.pl/soap")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }
}