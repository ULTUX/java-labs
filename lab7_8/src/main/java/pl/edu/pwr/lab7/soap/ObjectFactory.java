
package pl.edu.pwr.lab7.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import pl.edu.pwr.lab7.jpa.event.Event;
import pl.edu.pwr.lab7.jpa.installment.Installment;
import pl.edu.pwr.lab7.jpa.payment.Payment;
import pl.edu.pwr.lab7.jpa.person.Person;
import pl.edu.pwr.lab7.soap.payment.GetAllPaymentsRequest;
import pl.edu.pwr.lab7.soap.payment.GetAllPaymentsResponse;
import pl.edu.pwr.lab7.soap.person.*;


@XmlRegistry
public class ObjectFactory {

    private final static QName _AddPersonResponse_QNAME = new QName("http://pwr.edu.pl/soap", "addPersonResponse");
    private final static QName _GetAllPersonRequest_QNAME = new QName("http://pwr.edu.pl/soap", "getAllPersonRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.edu.pwr.lab7.soapgen
     * 
     */
    public ObjectFactory() {
    }

    public GetPersonRequest createGetPersonRequest() {
        return new GetPersonRequest();
    }

    public GetPersonResponse createGetPersonResponse() {
        return new GetPersonResponse();
    }

    /**
     * Create an instance of {@link Person }
     * 
     */
    public Person createPerson() {
        return new Person();
    }

    public SavePersonRequest createAddPersonRequest() {
        return new SavePersonRequest();
    }

    public GetAllPersonResponse createGetAllPersonResponse() {
        return new GetAllPersonResponse();
    }

    public GetPendingPeopleRequest createGetPendingPeopleRequest() {
        return new GetPendingPeopleRequest();
    }

    public GetPendingPersonResponse createGetPendingPeopleResponse() {
        return new GetPendingPersonResponse();
    }

    public GetAllPaymentsRequest createGetAllPaymentsRequest() {
        return new GetAllPaymentsRequest();
    }

    public GetAllPaymentsResponse createGetAllPaymentsResponse() {
        return new GetAllPaymentsResponse();
    }

    /**
     * Create an instance of {@link Payment }
     * 
     */
    public Payment createPayment() {
        return new Payment();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
     */
    @XmlElementDecl(namespace = "http://pwr.edu.pl/soap", name = "addPersonResponse")
    public JAXBElement<Object> createAddPersonResponse(Object value) {
        return new JAXBElement<Object>(_AddPersonResponse_QNAME, Object.class, null, value);
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
