package pl.edu.pwr.lab4;

import pl.edu.pwr.lab4.processing.Processor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProcessClassLoader extends ClassLoader {

    private final Path classDir;
    private final List<Processor> processors = new ArrayList<>();
    private final String packageName;

    public ProcessClassLoader(Path classDir, String packageName) {
        this.classDir = classDir;
        this.packageName = packageName;
    }

    public List<Processor> getClasses() throws IOException {
        try (
                URLClassLoader classLoader = new URLClassLoader(new URL[]{classDir.toUri().toURL()});
                Stream<Path> stream = Files.walk(classDir, 1)) {
            stream.forEach(path -> {
                if (!path.toFile().isDirectory()) {
                    try {
                        Class<?> objectClass = classLoader.loadClass(packageName + "."
                                + path.getFileName().toString().split("\\.")[0]);
                        if (Processor.class.isAssignableFrom(objectClass)) {
                            Processor customProcessor = (Processor) objectClass.getConstructor().newInstance();
                            processors.add(customProcessor);

                        }
                    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
                            | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return processors;
    }
}
