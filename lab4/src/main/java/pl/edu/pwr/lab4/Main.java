package pl.edu.pwr.lab4;

import pl.edu.pwr.lab4.processing.Processor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Processor> processors = new DirClassLoader("pl.edu.pwr.lab4.processors",
                    FileSystems.getDefault().getPath("/home/ultux/IdeaProjects/wnowak_252700_java/lab4/target/classes/pl/edu/pwr/lab4/processors/")).loadProcessorsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
