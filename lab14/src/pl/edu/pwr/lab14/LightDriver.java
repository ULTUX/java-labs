package pl.edu.pwr.lab14;

import pl.edu.pwr.lab14.ui.LightUI;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;


public class LightDriver implements LightDriverMBean {

    LightUI lights;

    private LightSequence lightSequence = null;


    public LightDriver(LightUI lights) {
        this.lights = lights;
    }

    @Override
    public void setSequence(String str) {
        lights.setSequence(str);
    }

    @Override
    public String getSequence() {
        var list = lightSequence.sequence.stream().map(Object::toString).collect(Collectors.toList());
        return String.join(" ", list);
    }

    @Override
    public void toggleLight(int lightId) {
        lights.toggleLight(lightId);
    }



    @Override
    public void toggleSimulation() {
        lights.toggleSimulation();
    }

    @Override
    public void initializeSequence() {
        lights.initializeSequence();
    }

    @Override
    public void changeLightCount(int count) {
        lights.setLightCount(count);
    }

    @Override
    public void changeSimulationInterval(int min, int max) {
        lights.setSimulationInterval(min, max);
    }
}
