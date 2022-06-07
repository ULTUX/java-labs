package pl.edu.pwr.lab14.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import pl.edu.pwr.lab14.LightDriver;
import pl.edu.pwr.lab14.LightSequence;

import javax.management.*;
import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.*;

public class LightInterface extends JFrame {

    private JPanel mainPanel;
    private JPanel canvasContainer;

    private Canvas canvas;

    private ScheduledExecutorService simulation;

    private LightSequence sequence;

    private static final Dimension CANVAS_SIZE = new Dimension(500, 100);

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        try {
            ObjectName objectName = new ObjectName("pl.edu.pwr.lab14.ui:type=basic,name=lightdriver");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(new LightDriver(new LightInterface()), objectName);
        } catch (MalformedObjectNameException | NotCompliantMBeanException | InstanceAlreadyExistsException |
                 MBeanRegistrationException e) {
            e.printStackTrace();
        }
    }

    public LightInterface() {
        super("Light simulation");
        this.canvas = new Canvas(CANVAS_SIZE);
        canvasContainer.add(canvas);
        canvasContainer.setPreferredSize(CANVAS_SIZE);

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        pack();

    }

    private void showNotification(String message) {
        JOptionPane.showMessageDialog(this, message, "Notification", JOptionPane.INFORMATION_MESSAGE);
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

    public void toggleSimulation() {
        if (sequence == null) return;
        if (simulation == null || simulation.isShutdown()){
            simulation = Executors.newSingleThreadScheduledExecutor();
            simulation.scheduleAtFixedRate(() -> {
                toggleLight(sequence.getNextInSequence());
            }, 0, 500, TimeUnit.MILLISECONDS);
        }
        else simulation.shutdown();
    }
}
