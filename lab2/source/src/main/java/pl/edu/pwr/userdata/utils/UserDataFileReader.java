package pl.edu.pwr.userdata.utils;

import pl.edu.pwr.userdata.datamodels.UserData;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserDataFileReader {
    private final File fileName;
    private UserData readUser;

    public UserDataFileReader(File fileName) throws IOException {
        this.fileName = fileName;
        readData();
    }

    private void readData() throws IOException {
        String imgPath = fileName.getAbsolutePath()+"/image.jpg";
        Path dataPath = FileSystems.getDefault().getPath(fileName.getAbsolutePath()+"/record.txt");

        List<String> lines = Files.readAllLines(dataPath);
        if (lines.size() != 3) throw new IOException("File structure not supported");
        String fName = lines.get(0).split(" ")[1];
        String lName = lines.get(1).split(" ")[1];
        int age = Integer.parseInt(lines.get(2).split(" ")[1]);

        readUser = new UserData(fName, lName, age, imgPath);
    }

    public UserData getReadUser() {
        return readUser;
    }
}
