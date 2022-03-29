package pl.edu.pwr.lab4.processors;

import pl.edu.pwr.lab4.processing.Processor;
import pl.edu.pwr.lab4.processing.Status;
import pl.edu.pwr.lab4.processing.StatusListener;
import pl.edu.pwr.lab4.processing.TaskIdDistributor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SumProcessor implements Processor {

    private String result;
    private final int taskId;

    public SumProcessor() {
        taskId = TaskIdDistributor.getInstance().registerNewProcessor(this);
    }

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        System.out.println("Submit task called");
        AtomicInteger timer = new AtomicInteger(0);
        ScheduledExecutorService timerS = Executors.newSingleThreadScheduledExecutor();
        timerS.scheduleAtFixedRate(() -> {
            try {
                timer.incrementAndGet();
                sl.statusChanged(new Status(taskId, timer.get()));
            }
            catch (Exception ignored){}
        }, 1, 10, TimeUnit.MILLISECONDS);

        ExecutorService taskS = Executors.newSingleThreadExecutor();
        taskS.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                    System.out.println(timer);
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
