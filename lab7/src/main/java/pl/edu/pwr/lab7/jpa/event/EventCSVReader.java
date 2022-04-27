package pl.edu.pwr.lab7.jpa.event;

import pl.edu.pwr.lab7.CSVReader;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventCSVReader extends CSVReader<Event> {

    protected EventCSVReader(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    @Override
    public List<Event> readData() throws IllegalArgumentException {
        List<Event> list = new ArrayList<>();
        for (String[] datum : this.data) {
            Event event = new Event();
            for (int j = 0; j < datum.length; j++) {
                switch (headers[j]) {
                    case "id":
                        event.setId(Integer.valueOf(datum[j]));
                        break;
                    case "time":
                        try {
                            event.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(datum[j]));
                        } catch (ParseException e) {
                            System.out.println("Could not read data");
                        }
                        break;
                    case "location":
                        event.setLocation(datum[j]);
                        break;
                    case "name":
                        event.setName(datum[j]);
                }
            }
            if (event.getTime() == null || event.getLocation() == null || event.getName() == null)
                throw new IllegalArgumentException();
            list.add(event);
        }
        return list;
    }
}
