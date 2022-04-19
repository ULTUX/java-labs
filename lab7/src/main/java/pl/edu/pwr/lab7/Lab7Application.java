package pl.edu.pwr.lab7;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.pwr.lab7.event.EventService;

@SpringBootApplication
@Configuration
public class Lab7Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab7Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(EventService service) {
        return args -> {
            service.importFromFile("C:\\Users\\ULTUX\\IdeaProjects\\wnowak_252700_java\\lab7\\src\\main\\java\\pl\\edu\\pwr\\lab7\\events.csv");
            System.out.println("TEST");
        };
    }

}
