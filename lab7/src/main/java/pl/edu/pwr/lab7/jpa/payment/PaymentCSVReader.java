package pl.edu.pwr.lab7.jpa.payment;

import pl.edu.pwr.lab7.CSVReader;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.person.PersonService;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentCSVReader extends CSVReader<Payment> {

    private PersonService personService;
    private InstallmentService installmentService;
    private EventService eventService;

    protected PaymentCSVReader(String fileName) throws FileNotFoundException {
        super(fileName);
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
                        payment.setTime(LocalDateTime.parse(datum[j]));
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
