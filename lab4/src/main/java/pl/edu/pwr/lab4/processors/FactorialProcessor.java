package pl.edu.pwr.lab4.processors;

import pl.edu.pwr.lab4.processing.Processor;
import pl.edu.pwr.lab4.processing.Status;
import pl.edu.pwr.lab4.processing.StatusListener;
import pl.edu.pwr.lab4.processing.TaskIdDistributor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FactorialProcessor implements Processor {

    private String result;
    private final int taskId;

    public FactorialProcessor() {
        taskId = TaskIdDistributor.getInstance().registerNewProcessor(this);
    }

    private int factorial(int n){
        if (n <= 2) {
            return n;
        }
        return n* factorial(n-1);
    }

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        AtomicInteger timer = new AtomicInteger(0);
        ScheduledExecutorService timerS = Executors.newSingleThreadScheduledExecutor();
        timerS.scheduleAtFixedRate(() -> {
            try {
                timer.incrementAndGet();
                sl.statusChanged(new Status(taskId, timer.get()));
            }
            catch (Exception ignored){}
        }, 1, 100, TimeUnit.MILLISECONDS);

        ExecutorService taskS = Executors.newSingleThreadExecutor();
        taskS.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException ignored){}
                if (timer.get() >= 100){
                    timerS.shutdown();
                    taskS.shutdown();
                    result = String.valueOf(factorial(Integer.parseInt(task.trim())));
                    break;

                }
            }
        });
        return true;
    }

    @Override
    public String getInfo() {
        return "Get factorial of number.";
    }

    @Override
    public String getResult() {
        return result;
    }
}
