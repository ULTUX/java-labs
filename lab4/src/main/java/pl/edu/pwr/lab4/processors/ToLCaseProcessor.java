package pl.edu.pwr.lab4.processors;

import pl.edu.pwr.lab4.processing.Processor;
import pl.edu.pwr.lab4.processing.Status;
import pl.edu.pwr.lab4.processing.StatusListener;
import pl.edu.pwr.lab4.processing.TaskIdDistributor;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ToLCaseProcessor implements Processor {

    private String result;
    private final int taskId;

    public ToLCaseProcessor() {
        taskId = TaskIdDistributor.getInstance().registerNewProcessor(this);
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
                    result = task.toLowerCase(Locale.ROOT);
                    timerS.shutdown();
                    taskS.shutdown();
                    break;

                }
            }
        });
        return true;
    }

    @Override
    public String getInfo() {
        return "Convert string to lower case";
    }

    @Override
    public String getResult() {
        if (result == null) return null;
        return new String(result);
    }
}
