package pl.edu.pwr.lab7.jpa.payment;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public void addPayment(Payment payment) {
        repository.save(payment);
    }

    public List<Payment> getAll() {
        return repository.findAll();
    }

    public void importFromFile(String fileName) {
        try {
            var reader = new PaymentCSVReader(fileName);
            repository.saveAll(reader.readData());
        } catch (FileNotFoundException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided fileName is not valid or file contents are not consistent.");
        }
    }

}
