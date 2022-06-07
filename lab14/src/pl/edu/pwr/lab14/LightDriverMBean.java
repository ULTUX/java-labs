package pl.edu.pwr.lab14;

public interface LightMBean {

    void setSequence(LightSequence sequence);

    LightSequence getSequence();

    void toggleLight(int lightId);

    void toggleSimulation();

}
