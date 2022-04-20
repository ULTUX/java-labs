package pl.edu.pwr.lab7.ui.dialogs;

import pl.edu.pwr.lab7.person.Person;
import pl.edu.pwr.lab7.person.PersonService;

import javax.swing.*;
import java.awt.event.*;

public class AddPersonDialog extends JDialog {
    private final transient PersonService personService;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fName;
    private JTextField lName;

    public AddPersonDialog(PersonService personService) {
        this.personService = personService;
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
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            var person = new Person();
            person.setFirstName(fName.getText());
            person.setLastName(lName.getText());
            personService.addPerson(person);
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not parse input data", "Error", JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
