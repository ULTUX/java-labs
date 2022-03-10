package pl.edu.pwr.userdata;

import java.awt.image.BufferedImage;

public class UserData {
    private final String name;
    private final String lastName;
    private final int age;
    private final BufferedImage profilePic;

    public UserData(String name, String lastName, int age, BufferedImage profilePic) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.profilePic = profilePic;
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

    public BufferedImage getProfilePic() {
        return profilePic;
    }
}
