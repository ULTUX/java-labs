package pl.edu.pwr.lab6.billboard;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        var frame = new JFrame();
        var manager = new Billboard(frame);
        frame.setTitle("Billboard");
        frame.setContentPane(manager.getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setVisible(true);
    }
}
