
package pl.edu.pwr.lab7.soap;

import pl.edu.pwr.lab7.jpa.event.Event;
import pl.edu.pwr.lab7.jpa.installment.Installment;
import pl.edu.pwr.lab7.jpa.payment.Payment;
import pl.edu.pwr.lab7.jpa.person.Person;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pl.edu.pwr.lab7.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetAllPersonRequest_QNAME = new QName("http://pwr.edu.pl/soap", "getAllPersonRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.edu.pwr.lab7.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PersonResponse }
     * 
     */
    public PersonResponse createGetPersonRequest() {
        return new PersonResponse();
    }

    /**
     * Create an instance of {@link PersonRequest }
     * 
     */
    public PersonRequest createGetPersonResponse() {
        return new PersonRequest();
    }

    /**
     * Create an instance of {@link Person }
     * 
     */
    public Person createPerson() {
        return new Person();
    }

    /**
     * Create an instance of {@link AllPersonResponse }
     * 
     */
    public AllPersonResponse createGetAllPersonResponse() {
        return new AllPersonResponse();
    }

    /**
     * Create an instance of {@link Event }
     * 
     */
    public Event createEvent() {
        return new Event();
    }

    /**
     * Create an instance of {@link Installment }
     * 
     */
    public Installment createInstallment() {
        return new Installment();
    }

    /**
     * Create an instance of {@link Payment }
     * 
     */
    public Payment createPayment() {
        return new Payment();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "http://pwr.edu.pl/soap", name = "getAllPersonRequest")
    public JAXBElement<Object> createGetAllPersonRequest(Object value) {
        return new JAXBElement<Object>(_GetAllPersonRequest_QNAME, Object.class, null, value);
    }

}
