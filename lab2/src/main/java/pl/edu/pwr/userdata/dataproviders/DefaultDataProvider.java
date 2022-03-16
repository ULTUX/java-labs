package pl.edu.pwr.userdata.dataproviders;

import pl.edu.pwr.userdata.datamodels.UserData;
import pl.edu.pwr.userdata.utils.UserDataFileReader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DefaultDataProvider implements IDataProvider {
    WeakHashMap<String, UserData> userData = new WeakHashMap<>();
    private JRadioButton notificationComponent;
    private final File rootDir;

    public DefaultDataProvider(File rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public UserData getUserData(String userId) throws IllegalArgumentException {
        if (userData.containsKey(userId)) {
            if (notificationComponent != null) notificationComponent.setSelected(false);
            return userData.get(userId);
        } else {
            try {
                var userReader = new UserDataFileReader(new File(rootDir, userId));
                var userData = userReader.getReadUser();
                if (notificationComponent != null) notificationComponent.setSelected(true);
                this.userData.put(userId, userData);
                return userData;
            } catch (IOException e) {
                throw new IllegalArgumentException("User with given ID does not exist");
            }
        }
    }

    @Override
    public List<String> getUserIds() {
        if (!rootDir.isDirectory()) throw new IllegalArgumentException("Method only accepts directories");
        var fileArray = rootDir.listFiles((dir, name) -> new File(dir, name).isDirectory());
        if (fileArray == null) return null;
        List<File> dirList = Arrays.asList(fileArray);
        var userIds = new ArrayList<String>();
        dirList.forEach(directory -> userIds.add(directory.getName()));
        return userIds;
    }

    @Override
    public void setNotificationComponent(JRadioButton notificationComponent) {
        this.notificationComponent = notificationComponent;

    }
}
