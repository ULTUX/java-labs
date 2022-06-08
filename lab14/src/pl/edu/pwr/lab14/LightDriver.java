package pl.edu.pwr.lab14;

import pl.edu.pwr.lab14.ui.LightUI;

import javax.management.AttributeChangeNotification;
import javax.management.NotificationBroadcasterSupport;
import java.util.Arrays;
import java.util.stream.Collectors;


public class LightDriver extends NotificationBroadcasterSupport implements LightDriverMBean {

    LightUI lights;

    private LightSequence sequence = new LightSequence(new Integer[]{});

    private int sequenceNumber = 0;


    public LightDriver(LightUI lights) {
        this.lights = lights;
    }

    @Override
    public void setSequence(String str) {
        var notification = new AttributeChangeNotification(
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "Sequence value changed",
                "sequence",
                "String",
                getSequence(),
                str
        );
        sendNotification(notification);
        this.sequence = new LightSequence(Arrays.stream(str.split(" ")).map(Integer::parseInt).filter(val -> val < lights.getLightCount()).toArray(Integer[]::new));
        lights.setSequence(this.sequence);
    }

    @Override
    public String getSequence() {
        var list = sequence.sequence.stream().map(Object::toString).collect(Collectors.toList());
        return String.join(" ", list);
    }

    @Override
    public void toggleLight(int lightId) {
        var notification = new AttributeChangeNotification(
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "Light was toggled",
                "lightId",
                "int",
                lights.getLightState(lightId),
                !lights.getLightState(lightId)
        );
        sendNotification(notification);
        lights.toggleLight(lightId);
    }



    @Override
    public void toggleSimulation() {
        var notification = new AttributeChangeNotification(
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "Simulation toggled",
                "simulation",
                "boolean",
                lights.getSimulationState(),
                !lights.getSimulationState()
        );
        sendNotification(notification);
        lights.toggleSimulation();
    }

    @Override
    public void initializeSequence() {
        var old = getSequence();
        lights.initializeSequence();
        this.sequence = lights.getSequence();
        var notification = new AttributeChangeNotification(
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "Sequence initialized",
                "sequence",
                "String",
                old,
                getSequence()

        );
        sendNotification(notification);
    }

    @Override
    public void changeLightCount(int count) {
        var old = lights.getLightCount();
        lights.setLightCount(count);
        var notification = new AttributeChangeNotification(
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "Light count changed",
                "sequence",
                "String",
                old,
                lights.getLightCount()
        );
        sendNotification(notification);
    }

    @Override
    public void changeSimulationInterval(int min, int max) {
        lights.setSimulationInterval(min, max);
    }
}
