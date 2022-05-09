package pl.edu.pwr.userdata.dataproviders;

import pl.edu.pwr.userdata.datamodels.UserData;

import javax.swing.*;
import java.util.List;

public interface IDataProvider {
    UserData getUserData(String userId) throws IllegalArgumentException;
    List<String> getUserIds();
    void setNotificationComponent(JRadioButton notificationComponent);
}
