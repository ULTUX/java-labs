package pl.edu.pwr.lab4.processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CustomStatusListener implements StatusListener{
    private HashMap<Processor, Status> statusMap = new HashMap<>();
    private List<Consumer<Status>> eventHandlers = new ArrayList<>();


    public void addEventHandler(Consumer<Status> r){
        eventHandlers.add(r);
    }

    public void removeEventHandler(Consumer<Status> r){
        eventHandlers.remove(r);
    }


    @Override
    public void statusChanged(Status s) {
        TaskIdDistributor distributor = TaskIdDistributor.getInstance();
        Processor processor = TaskIdDistributor.getInstance().getProcessorWithId(s.getTaskId());
        statusMap.put(processor, s);
        eventHandlers.forEach(statusConsumer -> statusConsumer.accept(s));
    }
}
