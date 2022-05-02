
package pl.edu.pwr.lab7.soap.person;

import pl.edu.pwr.lab7.jpa.person.Person;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="person" type="{http://pwr.edu.pl/soap}person"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id"
})
@XmlRootElement(name = "getPersonRequest", namespace = "http://pwr.edu.pl/soap")
public class GetPersonRequest {

    @XmlElement(required = true)
    protected int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
