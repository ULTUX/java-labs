package pl.edu.pwr.lab4.processors;

import pl.edu.pwr.lab4.processing.Processor;
import pl.edu.pwr.lab4.processing.Status;
import pl.edu.pwr.lab4.processing.StatusListener;
import pl.edu.pwr.lab4.processing.TaskIdDistributor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SumProcessor implements Processor {

    private String result;
    private int taskId;

    public SumProcessor() {
        taskId = TaskIdDistributor.getInstance().registerNewProcessor(this);
    }

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        AtomicInteger timer = new AtomicInteger(0);
        taskId++;
        ScheduledExecutorService timerS = Executors.newSingleThreadScheduledExecutor();
        timerS.scheduleAtFixedRate(() -> {
            timer.incrementAndGet();
            sl.statusChanged(new Status(taskId, timer.get()));
        }, 0, 10, TimeUnit.MILLISECONDS);

        ScheduledExecutorService taskS = Executors.newSingleThreadScheduledExecutor();
        taskS.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException ignored){}
                if (timer.get() >= 100){
                    String[] numbers = task.split("\\+");
                    System.out.println("Task finished");
                    timerS.shutdown();
                    taskS.shutdown();
                    result = String.valueOf(Integer.parseInt(numbers[0].trim()) + Integer.parseInt(numbers[1].trim()));
                    break;

                }
            }
        });
        return true;
    }

    @Override
    public String getInfo() {
        return "Sum two numbers passed in form \"x + y\".";
    }

    @Override
    public String getResult() {
        return result;
    }
}
