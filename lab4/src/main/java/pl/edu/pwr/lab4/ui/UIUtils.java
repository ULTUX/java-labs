package pl.edu.pwr.lab4.ui;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    private Component parent;

    public UIUtils(Component parent) {
        this.parent = parent;
    }

    public void showSuccessMessage(String message){
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
