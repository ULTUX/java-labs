package pl.edu.pwr.lab7.payment;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }
}
