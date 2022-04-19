package pl.edu.pwr.lab7.ui;

import pl.edu.pwr.lab7.event.EventService;
import pl.edu.pwr.lab7.installment.Installment;
import pl.edu.pwr.lab7.installment.InstallmentService;
import pl.edu.pwr.lab7.payment.PaymentService;
import pl.edu.pwr.lab7.person.PersonService;
import pl.edu.pwr.lab7.ui.dialogs.AddEventDialog;
import pl.edu.pwr.lab7.ui.dialogs.AddInstallmentDialog;
import pl.edu.pwr.lab7.ui.dialogs.AddPaymentDialog;
import pl.edu.pwr.lab7.ui.dialogs.AddPersonDialog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final PaymentService paymentService;
    private  final EventService eventService;
    private final PersonService personService;
    private final InstallmentService installmentService;

    private JTable pendingTable;
    private JTable paidTable;
    private JButton addButton;
    private JButton removeButton;
    private JButton importFromCSVButton;
    private JButton fastForwardButton;
    private JPanel mainPanel;
    List<Installment> pendingInstallments = new ArrayList<>();

    public MainFrame(PaymentService paymentService, EventService eventService, PersonService personService, InstallmentService installmentService){
        super("Statistic analysis tool");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();

        this.paymentService = paymentService;
        this.eventService = eventService;
        this.personService = personService;
        this.installmentService = installmentService;

        addButton.addActionListener(e -> addButtonClicked());
    }

    private void addButtonClicked() {
        var selection = (String) JOptionPane.showInputDialog(this, "What to add?", "Add...",
                JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Person", "Event", "Installment", "Payment"}, "Person");
        if (selection == null) return;
        switch (selection) {
            case "Person":
                var pDialog = new AddPersonDialog(personService);
                pDialog.pack();
                pDialog.setVisible(true);
                break;
            case "Event":
                var eDialog = new AddEventDialog(eventService);
                eDialog.pack();
                eDialog.setVisible(true);
                break;
            case "Installment":
                var iDialog = new AddInstallmentDialog(eventService, installmentService);
                iDialog.pack();
                iDialog.setVisible(true);
                break;
            case "Payment":
                var payment = new AddPaymentDialog(paymentService, personService, eventService, installmentService);
                payment.pack();
                payment.setVisible(true);
                break;
            default:
                throw new IllegalStateException("User did not select anything (that should be impossible");
        }
    }
}
