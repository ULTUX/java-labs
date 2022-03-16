package pl.edu.pwr.userdata.datamodels;

import javax.swing.*;
import java.awt.*;

public class UserData {
    private final String firstName;
    private final String lastName;
    private final int age;
    private final ImageIcon profilePic;

    public UserData(String firstName, String lastName, int age, String imagePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        ImageIcon icon = new ImageIcon(imagePath);
        this.profilePic = new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public ImageIcon getProfilePic() {
        return profilePic;
    }
}
