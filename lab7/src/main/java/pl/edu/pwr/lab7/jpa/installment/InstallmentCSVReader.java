package pl.edu.pwr.lab7.jpa.installment;

import pl.edu.pwr.lab7.CSVReader;
import pl.edu.pwr.lab7.jpa.event.EventService;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InstallmentCSVReader extends CSVReader<Installment> {

    private final EventService eventService;

    protected InstallmentCSVReader(String fileName, EventService eventService) throws FileNotFoundException {
        super(fileName);
        this.eventService = eventService;
    }

    @Override
    public List<Installment> readData() throws IllegalArgumentException {
        List<Installment> list = new ArrayList<>();
        for (String[] datum : this.data) {
            Installment installment = new Installment();
            for (int j = 0; j < datum.length; j++) {
                switch (headers[j]) {
                    case "installmentNum":
                        installment.setInstallmentNum(Integer.valueOf(datum[j]));
                        break;
                    case "amount":
                        installment.setAmount(Double.valueOf(datum[j]));
                        break;
                    case "event":
                        var event = eventService.getById(Integer.parseInt(datum[j]));
                        installment.setEvent(event);
                        break;
                    case "time":
                        installment.setTime(LocalDateTime.parse(datum[j]));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            if (installment.getTime() == null || installment.getInstallmentNum() == null || installment.getAmount() == null || installment.getEvent() == null)
                throw new IllegalArgumentException();
            list.add(installment);
        }
        return list;
    }

}

