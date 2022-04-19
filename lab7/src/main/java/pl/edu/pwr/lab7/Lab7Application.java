package pl.edu.pwr.lab7;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.pwr.lab7.event.EventService;
import pl.edu.pwr.lab7.installment.InstallmentService;
import pl.edu.pwr.lab7.payment.PaymentService;
import pl.edu.pwr.lab7.person.PersonService;
import pl.edu.pwr.lab7.ui.MainFrame;


@SpringBootApplication
@Configuration
public class Lab7Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Lab7Application.class).headless(false).run(args);
    }

    @Bean
    CommandLineRunner commandLineRunner(EventService eventService, PersonService personService,
                                        InstallmentService installmentService, PaymentService paymentService) {
        return args -> {
            new MainFrame(paymentService, eventService, personService, installmentService);
        };
    }

}
