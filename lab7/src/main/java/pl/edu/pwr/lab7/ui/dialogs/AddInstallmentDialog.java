package pl.edu.pwr.lab7.ui.dialogs;

import pl.edu.pwr.lab7.jpa.event.Event;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.Installment;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;

public class AddInstallmentDialog extends JDialog {
    private final transient InstallmentService installmentService;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner installmentNum;
    private JTextField paymentTime;
    private JTextField amount;
    private JComboBox<Event> eventSelector;

    public AddInstallmentDialog(EventService eventService, InstallmentService installmentService) {
        this.installmentService = installmentService;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        List<Event> eventList = eventService.getAll();
        eventSelector.setModel(new DefaultComboBoxModel<>(eventList.toArray(new Event[0])));

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        var installment = new Installment();
        try {
            installment.setInstallmentNum((Integer) installmentNum.getValue());
            installment.setTime(LocalDateTime.parse(paymentTime.getText()));
            installment.setEvent((Event) eventSelector.getSelectedItem());
            installment.setAmount(Double.valueOf(amount.getText()));
            installmentService.addInstallment(installment);
            dispose();
        }
        catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not parse input data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

}
