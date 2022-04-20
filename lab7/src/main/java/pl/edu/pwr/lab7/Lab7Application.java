package pl.edu.pwr.lab7;

import com.formdev.flatlaf.FlatDarkLaf;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;


@SpringBootApplication
@Configuration
public class Lab7Application {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Failed to initialize flatlaf.");
        }
        new SpringApplicationBuilder(Lab7Application.class).headless(false).build(args).run();
    }

}
