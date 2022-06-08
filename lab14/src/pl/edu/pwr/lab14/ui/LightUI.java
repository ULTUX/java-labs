package pl.edu.pwr.lab14.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import pl.edu.pwr.lab14.LightDriver;
import pl.edu.pwr.lab14.LightSequence;

import javax.management.*;
import javax.swing.*;
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

    public static void main(String[] args) {


        FlatDarkLaf.setup();
        try {
            ObjectName objectName = new ObjectName("pl.edu.pwr.lab14.ui:type=basic,name=lightdriver");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            var listener = new LightUI();
            var bean = new LightDriver(listener);
            bean.addNotificationListener(listener, null, null);
            server.registerMBean(bean, objectName);
        } catch (MalformedObjectNameException | NotCompliantMBeanException | InstanceAlreadyExistsException |
                 MBeanRegistrationException e) {
            e.printStackTrace();
        }


    }

    public LightUI() {
        super("Light simulation");
        this.canvas = new Canvas(CANVAS_SIZE, LIGHT_COUNT, mainPanel);
        mainPanel.add(canvas);
        mainPanel.setPreferredSize(CANVAS_SIZE);
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        pack();

        initButton.addActionListener(e -> initializeSequence());
        toggleButton.addActionListener(e -> toggleSimulation());
        setSequenceButton.addActionListener(e -> handleSetSequence());
        setLightCountButton.addActionListener(e -> handleSetLightCount());
        setIntervalButton.addActionListener(e -> handleSetInterval());
    }

    private void handleSetInterval() {
        var minInterval = getIntUserInput("New min interval");
        if (minInterval == -1) return;
        var maxInterval = getIntUserInput("New max interval");
        if (maxInterval == -1) return;
        setSimulationInterval(minInterval, maxInterval);
    }

    public void setSimulationInterval(int minInterval, int maxInterval) {
        this.minSimulationInterval = minInterval;
        this.maxSimulationInterval = maxInterval;
        toggleSimulation();
        toggleSimulation();
    }

    private void handleSetLightCount() {
        var count = JOptionPane.showInputDialog(this, "What new light count should be?", "Light count input", JOptionPane.QUESTION_MESSAGE);
        if (count == null || count.trim().equals("")) return;
        try {
            setLightCount(Integer.parseInt(count));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Provided input is not integer", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            setSequence(input);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Provided input is not valid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initializeSequence() {
        sequence = new LightSequence(random.ints(getLightCount(), 0, getLightCount()).boxed().toArray(Integer[]::new));
    }


    private void showNotification(String message) {
        JOptionPane.showMessageDialog(null, message, "Notification", JOptionPane.INFORMATION_MESSAGE);
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
        if (simulation == null || simulation.isShutdown()){
            simulation = Executors.newSingleThreadScheduledExecutor();
            simulation.scheduleAtFixedRate(() ->
                    toggleLight(sequence.getNextInSequence()), 0, ThreadLocalRandom.current().nextInt(minSimulationInterval, maxSimulationInterval), TimeUnit.MILLISECONDS);
        }
        else simulation.shutdown();
    }

    public int getLightCount() {
        return canvas.getCurrentLightCount();
    }

    public void setLightCount(int count) {
        var newSize = canvas.setLightCount(count);
        SwingUtilities.invokeLater(() -> {
            setPreferredSize(new Dimension(newSize.width+10, 230));
            pack();
        });
    }

    public boolean getLightState(int lightId) {
        return canvas.getLightState(lightId);
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "New notification: "+notification.getMessage(), "Notification", JOptionPane.INFORMATION_MESSAGE));
    }
}
