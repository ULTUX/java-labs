package pl.edu.pwr.lab7;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class CSVReader<T> {
    protected Scanner inputStream;
    protected String[][] data;
    protected String[] headers;

    protected CSVReader(String fileName) throws FileNotFoundException {
        this.inputStream = new Scanner(new BufferedInputStream(new FileInputStream(fileName)));
        headers = inputStream.nextLine().split(",");
        List<String[]> dataLines = new ArrayList<>();
        while (inputStream.hasNext()){
            dataLines.add(inputStream.nextLine().split(","));
        }
        data = dataLines.toArray(new String[0][0]);
    }

    public abstract List<T> readData();
}
