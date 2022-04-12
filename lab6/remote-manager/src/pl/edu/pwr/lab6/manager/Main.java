package pl.edu.pwr.lab6.manager;

import javax.swing.*;
import java.rmi.RMISecurityManager;

public class Main {
    public static void main(String[] args) throws Exception {
        var manager = new Manager();
        var frame = new JFrame();
        frame.setTitle("Manager");
        frame.setContentPane(manager.getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setVisible(true);
    }
}
