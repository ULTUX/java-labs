package pl.edu.pwr.lab7.person;

import pl.edu.pwr.lab7.CSVReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PersonCSVReader extends CSVReader<Person> {

    protected PersonCSVReader(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    @Override
    public List<Person> readData() throws IllegalArgumentException {
        List<Person> list = new ArrayList<>();
        for (String[] datum : this.data) {
            Person person = new Person();
            for (int j = 0; j < datum.length; j++) {
                switch (headers[j]) {
                    case "firstName":
                        person.setFirstName(datum[j]);
                        break;
                    case "lastName":
                        person.setLastName(datum[j]);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            if (person.getFirstName() == null || person.getLastName() == null) throw new IllegalArgumentException();
            list.add(person);
        }
        return list;
    }
}
