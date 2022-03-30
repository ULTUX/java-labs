package pl.edu.pwr.lab4;

import pl.edu.pwr.lab4.processing.Processor;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DirClassLoader extends ClassLoader{
    private String classPackageName;
    private Path classesPath;

    public DirClassLoader(String classPackageName, Path classesPath) {
        this.classPackageName = classPackageName;
        this.classesPath = classesPath;
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] b = new byte[0];
        try {
            b = loadClassFromFile(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String className = classPackageName+"."+name.substring(0, name.length()-6);
        return defineClass(className, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String name) throws IOException {
        try (InputStream stream = new FileInputStream(classesPath+File.separator+name)){
            byte[] buffer;
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextVal = 0;
            while ((nextVal = stream.read()) != -1) {
                byteStream.write(nextVal);
            }
            buffer = byteStream.toByteArray();
            return buffer;
        }
    }

    public List<Class<Processor>> loadProcessorsFromFile() throws IOException {
        List<Class<Processor>> processors = new ArrayList<>();
        try (
                Stream<Path> stream = Files.walk(classesPath, 1)) {
            stream.forEach(path -> {
                if (!path.toFile().isDirectory()) {
                    try {
                        Class<?> objectClass = loadClass(path.getFileName().toString());
                        if (Processor.class.isAssignableFrom(objectClass)) {
                            processors.add((Class<Processor>) objectClass);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return processors;
    }


}
