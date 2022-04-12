package pl.edu.pwr.lab6.manager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String regName = JOptionPane.showInputDialog(null, "Please provide manager registry name");
        var manager = new Manager(regName);
        var frame = new JFrame();
        frame.setTitle("Manager");
        frame.setContentPane(manager.getPanel1());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setVisible(true);
    }
}
