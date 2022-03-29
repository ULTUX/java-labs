package pl.edu.pwr.lab4.ui;

import pl.edu.pwr.lab4.processing.Processor;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class AddNewTaskDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<AddNewTaskDialog.ProcessorListEntity> taskList;
    DefaultListModel<AddNewTaskDialog.ProcessorListEntity> model;
    private JTextField taskInput;
    private String processorInput;
    private Processor selectedProcessor;
    private boolean approved = false;


    UIUtils uiUtils = new UIUtils(this);

    public AddNewTaskDialog(List<Processor> processors) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        if (processors == null || processors.isEmpty()) return;
        List<AddNewTaskDialog.ProcessorListEntity> processorsListE = processors.stream().map(AddNewTaskDialog.ProcessorListEntity::new).collect(Collectors.toList());
        model = new DefaultListModel<>();
        processorsListE.forEach(model::addElement);
        taskList.setModel(model);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (taskInput.getText().trim().equals("") || taskList.getSelectedIndex() == -1) {
            uiUtils.showErrorMessage("You did not select any task/input is empty.");
            return;
        }
        approved = true;
        this.selectedProcessor = taskList.getSelectedValue().processor;
        this.processorInput = taskInput.getText();
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public String getProcessorInput() {
        return this.processorInput;
    }
    public Processor getSelectedProcessor() {
        return selectedProcessor;
    }

    public boolean isApproved() {
        return approved;
    }

    public static class ProcessorListEntity {
        public final Processor processor;
        public final String name;
        public final String info;


        public ProcessorListEntity(Processor processor) {
            this.processor = processor;
            this.name = processor.getClass().getName();
            this.info = processor.getInfo();
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
