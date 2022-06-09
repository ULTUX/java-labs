package pl.edu.pwr.lab14.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import pl.edu.pwr.lab14.LightDriver;
import pl.edu.pwr.lab14.LightDriverMBean;
import pl.edu.pwr.lab14.LightSequence;

import javax.management.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class LightUI extends JFrame implements NotificationListener {

    private JPanel mainPanel;
    private JButton initButton;
    private JButton toggleButton;
    private JButton setSequenceButton;
    private JButton setLightCountButton;
    private JButton setIntervalButton;
    private Canvas canvas;
    private ScheduledExecutorService simulation;
    private LightSequence sequence;
    Random random = new Random();

    private int minSimulationInterval = 100;
    private int maxSimulationInterval = 500;

    private static final Dimension CANVAS_SIZE = new Dimension(500, 200);

    private static final int LIGHT_COUNT = 9;

    private final LightDriverMBean bean;

    public static void main(String[] args) {


        FlatDarkLaf.setup();
        new LightUI();

    }

    public LightUI() {
        super("Light simulation");

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        bean = new LightDriver(this);
        ((LightDriver) bean).addNotificationListener(this, null, null);
        try {
            ObjectName objectName = new ObjectName("pl.edu.pwr.lab14.ui:type=basic,name=lightdriver");
            server.registerMBean(bean, objectName);
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException |
                 MalformedObjectNameException e) {
            JOptionPane.showMessageDialog(this, "Could not initialize bean.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        this.canvas = new Canvas(CANVAS_SIZE, LIGHT_COUNT, mainPanel);
        mainPanel.add(canvas);
        mainPanel.setPreferredSize(CANVAS_SIZE);
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        pack();

        initButton.addActionListener(e -> bean.initializeSequence());
        toggleButton.addActionListener(e -> bean.toggleSimulation());
        setSequenceButton.addActionListener(e -> handleSetSequence());
        setLightCountButton.addActionListener(e -> handleSetLightCount());
        setIntervalButton.addActionListener(e -> handleSetInterval());
    }

    private void handleSetInterval() {
        var minInterval = getIntUserInput("New min interval");
        if (minInterval == -1) return;
        var maxInterval = getIntUserInput("New max interval");
        if (maxInterval == -1) return;
        bean.changeSimulationInterval(minInterval, maxInterval);
    }

    public void setSimulationInterval(int minInterval, int maxInterval) {
        this.minSimulationInterval = minInterval;
        this.maxSimulationInterval = maxInterval;
        toggleSimulation();
        toggleSimulation();
    }

    private void handleSetLightCount() {
        var count = getIntUserInput("What new light count should be?");
        if (count == -1) return;
        bean.changeLightCount(count);
    }

    private int getIntUserInput(String message) {
        var val = JOptionPane.showInputDialog(this, message, "Integer input", JOptionPane.QUESTION_MESSAGE);
        if (val == null || val.trim().equals("")) return -1;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Provided input is not integer", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    private void handleSetSequence() {
        try {
            var input = JOptionPane.showInputDialog(this, "Please provide new sequence for lights", "Sequence input", JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.trim().equals("")) return;
            bean.setSequence(input);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Provided input is not valid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initializeSequence() {
        sequence = new LightSequence(random.ints(getLightCount(), 0, getLightCount()).boxed().toArray(Integer[]::new));
    }


    public void setSequence(String sequence) {
        this.sequence = new LightSequence(Arrays.stream(sequence.split(" ")).map(Integer::parseInt).filter(val -> val < getLightCount()).toArray(Integer[]::new));
    }

    public void setSequence(LightSequence sequence) {
        this.sequence = sequence;
    }

    public LightSequence getSequence() {
        return this.sequence;
    }

    public void toggleLight(int lightId) {
        canvas.toggleLight(lightId);
    }

    public boolean getSimulationState() {
        if (simulation == null || simulation.isShutdown()) return false;
        return true;
    }

    public void toggleSimulation() {
        if (sequence == null) return;
        if (simulation == null || simulation.isShutdown()) {
            simulation = Executors.newSingleThreadScheduledExecutor();
            simulation.scheduleAtFixedRate(() ->
                    toggleLight(sequence.getNextInSequence()), 0, ThreadLocalRandom.current().nextInt(minSimulationInterval, maxSimulationInterval), TimeUnit.MILLISECONDS);
        } else simulation.shutdown();
    }

    public int getLightCount() {
        return canvas.getCurrentLightCount();
    }

    public void setLightCount(int count) {
        var newSize = canvas.setLightCount(count);
        SwingUtilities.invokeLater(() -> {
            setPreferredSize(new Dimension(Math.max(newSize.width + 10, 377), 230));
            pack();
        });
    }

    public boolean getLightState(int lightId) {
        return canvas.getLightState(lightId);
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "New notification: " + notification.getMessage(), "Notification", JOptionPane.INFORMATION_MESSAGE));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, BorderLayout.SOUTH);
        final JSeparator separator1 = new JSeparator();
        panel1.add(separator1, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        setLightCountButton = new JButton();
        setLightCountButton.setText("Set light count");
        panel1.add(setLightCountButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setIntervalButton = new JButton();
        setIntervalButton.setText("Set interval");
        panel1.add(setIntervalButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        setSequenceButton = new JButton();
        setSequenceButton.setText("Set sequence");
        panel1.add(setSequenceButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        toggleButton = new JButton();
        toggleButton.setText("Toggle");
        panel1.add(toggleButton, new GridConstraints(1, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        initButton = new JButton();
        initButton.setText("Init");
        panel1.add(initButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
