package pl.edu.pwr.lab7.ui;

import org.springframework.stereotype.Service;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.jpa.installment.Installment;
import pl.edu.pwr.lab7.jpa.installment.InstallmentService;
import pl.edu.pwr.lab7.jpa.payment.Payment;
import pl.edu.pwr.lab7.jpa.payment.PaymentService;
import pl.edu.pwr.lab7.jpa.person.PersonService;
import pl.edu.pwr.lab7.ui.dialogs.AddEventDialog;
import pl.edu.pwr.lab7.ui.dialogs.AddInstallmentDialog;
import pl.edu.pwr.lab7.ui.dialogs.AddPaymentDialog;
import pl.edu.pwr.lab7.ui.dialogs.AddPersonDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class MainFrame extends JFrame {

    private final transient PaymentService paymentService;

    private final transient EventService eventService;
    private final transient PersonService personService;
    private final transient InstallmentService installmentService;

    private JTable pendingTable;
    private JTable paidTable;
    private JButton addButton;
    private JButton importFromCSVButton;
    private JButton fastForwardButton;
    private JPanel mainPanel;

    private final transient List<Installment> pendingList = new ArrayList<>();
    private final transient List<Payment> paidList = new ArrayList<>();

    public MainFrame(
            PaymentService paymentService,
            EventService eventService,
            PersonService personService,
            InstallmentService installmentService
    ) {
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

        fastForwardButton.addActionListener(e -> ffClicked());
        importFromCSVButton.addActionListener(e -> importFromCSVClicked());
        reloadTables();
    }

    private void ffClicked() {
        var providedTime = JOptionPane.showInputDialog(this,
                "Please provide new time to fast forward to.",
                "Fast forward", JOptionPane.QUESTION_MESSAGE);
        try {
            final Date time = new SimpleDateFormat("dd-MM-yyyy").parse(providedTime);
            pendingList.forEach(installment -> {
                if (time.compareTo(installment.getTime()) > 0) {
                    var pendingPeople = personService.getPendingPeople();
                    pendingPeople.forEach(person -> {
                        var partial = paidList
                                .stream()
                                .filter(payment ->
                                        payment.getPerson().getId().equals(person.getId())
                                                && payment.getInstallment().getId().equals(installment.getId()))
                                .findAny()
                                .map(Payment::getAmount)
                                .orElse(0.0d);

                        JOptionPane.showMessageDialog(this, "Person: "
                                + person.getFirstName()
                                + " "
                                + person.getLastName() + " did not (fully) pay installment: "
                                + installment.getInstallmentNum()
                                + " (" + (installment.getAmount() - partial)
                                + "$).");
                    });
                }
            });
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not properly parse given date",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void reloadTables() {
        var payments = paymentService.getAll();
        var pending = installmentService.getAll().toArray(Installment[]::new);
        pendingList.clear();
        paidList.clear();
        pendingList.addAll(Arrays.asList(pending));
        paidList.addAll(payments);
        List<String[]> pendingSplit = new ArrayList<>();
        for (Installment curr : pending) {
            pendingSplit.add(new String[]{curr.getEvent().getName(), String.valueOf(curr.getInstallmentNum()),
                    String.valueOf(curr.getTime()).replace('T', ' '), String.valueOf(curr.getAmount())});
        }
        var pendingTableModel = new DefaultTableModel(pendingSplit.toArray(
                new String[pendingSplit.size()][]),
                new String[]{"Event", "Number", "Date", "Amount"});

        pendingTable.setModel(pendingTableModel);

        List<String[]> paidSplit = new ArrayList<>();
        for (Payment curr : payments) {
            paidSplit.add(
                    new String[]{String.valueOf(curr.getTime()).replace('T', ' '),
                            String.valueOf(curr.getAmount()),
                            curr.getPerson().getFirstName() + " " + curr.getPerson().getLastName(),
                            curr.getEvent().getName(),
                            String.valueOf(curr.getInstallment().getInstallmentNum())});
        }

        var paidTableModel = new DefaultTableModel(
                paidSplit.toArray(new String[paidSplit.size()][]),
                new String[]{"Date", "Amount", "Person", "Event", "Installment"}
        );
        paidTable.setModel(paidTableModel);
    }

    private void importFromCSVClicked() {
        var selection = (String) JOptionPane.showInputDialog(
                this,
                "What to import?",
                "Import",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Person", "Event", "Installment", "Payment"},
                "Person");

        if (selection == null) return;
        var fileSelector = new JFileChooser();
        fileSelector.setAcceptAllFileFilterUsed(false);
        fileSelector.setMultiSelectionEnabled(false);
        fileSelector.setFileFilter(new FileNameExtensionFilter("CSV files (.csv)", "csv"));
        var file = fileSelector.showOpenDialog(this);
        if (file != JFileChooser.APPROVE_OPTION) return;
        var filePath = fileSelector.getSelectedFile().toPath().toAbsolutePath().toString();
        try {
        switch (selection) {
            case "Person":
                personService.importFromFile(filePath);
                break;
            case "Event":
                eventService.importFromFile(filePath);
                break;
            case "Installment":
                installmentService.importFromFile(filePath);
                break;
            case "Payment":
                paymentService.importFromFile(filePath);
                break;
            default:
                throw new IllegalStateException("User did not select anything (that should be impossible");
        }
        JOptionPane.showMessageDialog(
                this,
                "Successfully imported!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not import selected file, contents may be invalid.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addButtonClicked() {
        var selection = (String) JOptionPane.showInputDialog(
                this,
                "What to add?",
                "Add...",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Person", "Event", "Installment", "Payment"},
                "Person"
        );
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
        reloadTables();
    }
}
