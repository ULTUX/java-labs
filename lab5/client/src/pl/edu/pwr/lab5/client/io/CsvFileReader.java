package pl.edu.pwr.lab5.client.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvFileReader {
    private final File file;
    private String[] headers;
    private String[][] data;

    public CsvFileReader(File file) {
        this.file = file;
    }

    public void readFile() throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)))){
            String[] fileHeaders = scanner.nextLine().split(",");
            List<String[]> fileData = new ArrayList<>();
            while (scanner.hasNextLine()){
                String row = scanner.nextLine();
                fileData.add(row.split(","));
            }
            String[][] dataArray = fileData.toArray(new String[][]{});
            this.headers = fileHeaders;
            this.data = dataArray;
        }
    }

    public File getFile() {
        return file;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String[][] getData() {
        return data;
    }
}
