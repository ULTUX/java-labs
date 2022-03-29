package pl.edu.pwr.lab4.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomStatusListener implements StatusListener{
    private HashMap<Processor, Status> statusMap;
    private List<Runnable> eventHandlers = new ArrayList<>();


    public void addEventHandler(Runnable r){
        eventHandlers.add(r);
    }

    public void removeEventHandler(Runnable r){
        eventHandlers.remove(r);
    }


    @Override
    public void statusChanged(Status s) {
        Processor processor = TaskIdDistributor.getInstance().getProcessorWithId(s.getTaskId());
        statusMap.put(processor, s);
        eventHandlers.forEach(Runnable::run);
    }
}
