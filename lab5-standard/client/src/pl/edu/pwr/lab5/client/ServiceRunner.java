package pl.edu.pwr.lab5.client;

import pl.edu.pwr.lab5.api.AnalysisException;
import pl.edu.pwr.lab5.api.AnalysisService;
import pl.edu.pwr.lab5.api.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class ServiceRunner {
    private final List<Consumer<Object>> calculationsFinishListeners = new ArrayList<>();
    private DataSet inputData;
    private AnalysisService service;
    private final Random random = new Random();


    public void startCalc() {
        Thread calculations = new Thread(() -> {
            try {
                service.submit(inputData);
                var output = service.retrieve(false);
                Thread.sleep(random.nextInt(1001));
                calculationsFinishListeners.forEach(objectConsumer -> objectConsumer.accept(output));
            } catch (AnalysisException | InterruptedException e) {
                calculationsFinishListeners.forEach(objectConsumer -> objectConsumer.accept(e));
                Thread.currentThread().interrupt();
            }
        });
        calculations.start();
    }

    public void addListener(Consumer<Object> listener) {
        calculationsFinishListeners.add(listener);
    }

    public void removeListener(Consumer<Object> listener) {
        calculationsFinishListeners.remove(listener);
    }

    public DataSet getInputData() {
        return inputData;
    }

    public void setInputData(DataSet inputData) {
        this.inputData = inputData;
    }

    public AnalysisService getService() {
        return service;
    }

    public void setService(AnalysisService service) {
        this.service = service;
    }
}
