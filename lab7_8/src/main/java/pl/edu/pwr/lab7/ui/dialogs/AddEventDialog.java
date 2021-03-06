package pl.edu.pwr.lab7.ui.dialogs;

import pl.edu.pwr.lab7.jpa.event.Event;
import pl.edu.pwr.lab7.jpa.event.EventService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventDialog extends JDialog {
    private final transient EventService eventService;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField location;
    private JTextField time;
    private JTextField eventName;

    public AddEventDialog(EventService eventService) {
        this.eventService = eventService;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            var event = new Event();
            event.setName(eventName.getText());
            event.setLocation(location.getText());
            event.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(time.getText()));
            eventService.addEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Could not parse input data",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
