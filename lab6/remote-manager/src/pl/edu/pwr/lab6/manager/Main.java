package pl.edu.pwr.lab6.manager;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        var manager = new Manager();
        var frame = new JFrame();
        frame.setContentPane(manager.getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setVisible(true);
    }
}
