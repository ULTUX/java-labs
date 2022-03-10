package pl.edu.pwr.userdata;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UserData {
    private final String name;
    private final String lastName;
    private final int age;
    private final ImageIcon profilePic;

    public UserData(String name, String lastName, int age, String imagePath) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        ImageIcon icon = new ImageIcon(imagePath);
        this.profilePic = new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
    }

    public String getName() {
        return name;
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
