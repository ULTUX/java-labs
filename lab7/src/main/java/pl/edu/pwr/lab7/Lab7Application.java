package pl.edu.pwr.lab7;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
public class Lab7Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Lab7Application.class).headless(false).run(args);
    }

}
