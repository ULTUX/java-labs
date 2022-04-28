package pl.edu.pwr.lab7.ui.dialogs;

import pl.edu.pwr.lab7.jpa.event.Event;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.Installment;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.payment.Payment;
import pl.edu.pwr.lab7.jpa.payment.PaymentService;
import pl.edu.pwr.lab7.jpa.person.Person;
import pl.edu.pwr.lab7.jpa.person.PersonService;

import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AddPaymentDialog extends JDialog {
    private final transient PaymentService paymentService;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField paymentTime;
    private JTextField amount;
    private JComboBox<Person> personSelector;
    private JComboBox<Event> eventSelector;
    private JComboBox<Installment> installmentSelector;

    public AddPaymentDialog(
            PaymentService paymentService,
            PersonService personService,
            EventService eventService,
            InstallmentService installmentService
    ) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.paymentService = paymentService;
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        paymentTime.setText(LocalDate.now().format(formatter));

        personSelector.setModel(new DefaultComboBoxModel<>(personService.getAll().toArray(new Person[0])));
        eventSelector.setModel(new DefaultComboBoxModel<>(eventService.getAll().toArray(new Event[0])));
        installmentSelector.setModel(new DefaultComboBoxModel<>(installmentService.getAll().toArray(new Installment[0])));

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
        // add your code here
        try {
            var payment = new Payment();
            payment.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(paymentTime.getText()));
            payment.setEvent((Event) eventSelector.getSelectedItem());
            payment.setInstallment((Installment) installmentSelector.getSelectedItem());
            payment.setAmount(Double.valueOf(amount.getText()));
            payment.setPerson((Person) personSelector.getSelectedItem());
            paymentService.addPayment(payment);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Could not parse input data",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
