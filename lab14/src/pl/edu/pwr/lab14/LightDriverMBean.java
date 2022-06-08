package pl.edu.pwr.lab14;

public interface LightDriverMBean {

    void setSequence(String sequence);

    String getSequence();

    void toggleLight(int lightId);

    void toggleSimulation();

    void initializeSequence();

    void changeLightCount(int count);

    void changeSimulationInterval(int min, int max);

}
