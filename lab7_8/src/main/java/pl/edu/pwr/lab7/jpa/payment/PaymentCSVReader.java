package pl.edu.pwr.lab7.jpa.payment;

import pl.edu.pwr.lab7.CSVReader;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.person.PersonService;

import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentCSVReader extends CSVReader<Payment> {

    private final PersonService personService;
    private final InstallmentService installmentService;
    private final EventService eventService;

    protected PaymentCSVReader(String fileName, PersonService personService, InstallmentService installmentService, EventService eventService) throws FileNotFoundException {
        super(fileName);
        this.personService = personService;
        this.installmentService = installmentService;
        this.eventService = eventService;
    }

    @Override
    public List<Payment> readData() throws IllegalArgumentException {
        List<Payment> list = new ArrayList<>();
        for (String[] datum : this.data) {
            Payment payment = new Payment();
            for (int j = 0; j < datum.length; j++) {
                switch (headers[j]) {
                    case "amount":
                        payment.setAmount(Double.valueOf(datum[j]));
                        break;
                    case "event":
                        var event = eventService.getById(Integer.parseInt(datum[j]));
                        payment.setEvent(event);
                        break;
                    case "installment":
                        var installment = installmentService.getById(Integer.parseInt(datum[j]));
                        payment.setInstallment(installment);
                        break;
                    case "time":
                        try {
                            payment.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(datum[j]));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "person":
                        var person = personService.getById(Integer.parseInt(datum[j]));
                        payment.setPerson(person);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            if (payment.getTime() == null || payment.getInstallment() == null || payment.getAmount() == null
                    || payment.getEvent() == null || payment.getPerson() == null)
                throw new IllegalArgumentException();
            list.add(payment);
        }
        return list;
    }
}
