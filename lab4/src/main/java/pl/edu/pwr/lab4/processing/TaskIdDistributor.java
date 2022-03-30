package pl.edu.pwr.lab4.processing;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskIdDistributor {
    private static TaskIdDistributor instance;
    private int currentId;
    private HashMap<Integer, Processor> runningProcessors = new HashMap<>();

    public static TaskIdDistributor getInstance() {
        if (instance == null) instance = new TaskIdDistributor();
        return instance;
    }

    public int registerNewProcessor(Processor processor) {
        runningProcessors.put(currentId, processor);
        return currentId++;
    }

    public void clearRunningProcessors() {
        runningProcessors.clear();
        runningProcessors = new HashMap<>();
    }

    public int getId(Processor processor) {
        if (processor == null) return -1;
        AtomicInteger id = new AtomicInteger(-1);
        runningProcessors.forEach((integer, processor1) -> {
            if (processor.equals(processor1)) id.set(integer);
        });
        return id.get();
    }


    public void unregisterProcessor(Processor processor){
        int key = getId(processor);
        if (key != -1) {
            runningProcessors.remove(key);
        }
    }

    public Processor getProcessorWithId(int id) {
        return runningProcessors.get(id);
    }

}
