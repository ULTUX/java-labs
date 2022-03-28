package pl.edu.pwr.lab4;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = FileSystems.getDefault().getPath("/home/ultux/IdeaProjects/wnowak_252700_java/lab4/target/classes/pl/edu/pwr/lab4/example");
        ProcessClassLoader loader = new ProcessClassLoader(path, "pl.edu.pwr.lab4.processors");
        try {
            loader.getClasses().forEach(processor -> {
                System.out.println(processor.getInfo());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
