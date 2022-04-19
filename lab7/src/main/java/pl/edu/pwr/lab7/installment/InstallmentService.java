package pl.edu.pwr.lab7.installment;

import org.springframework.stereotype.Service;

@Service
public class InstallmentService {

    private final InstallmentRepository repository;

    public InstallmentService(InstallmentRepository repository) {
        this.repository = repository;
    }
}
