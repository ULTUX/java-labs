package pl.edu.pwr.lab4.processors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


import pl.edu.pwr.lab4.processing.Processor;
import pl.edu.pwr.lab4.processing.Status;
import pl.edu.pwr.lab4.processing.StatusListener;

/**
 * Klasa procesora symuluj?ca d?ugotrwa?e przetwarzanie
 * @author tkubik
 *
 */
		
public class MyProcessor implements Processor {
	
	private static int taskId=0;
	private String result = null;

	@Override
	public boolean submitTask(String task, StatusListener sl) {
		taskId++;
		AtomicInteger ai = new AtomicInteger(0);
		 	
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		// Aby zasymulowa? d?ugotrwa?e przetwarzanie uruchamiane s? w niesko?czono??
		// kr?tkie zadania inkrementuj?ce licznik i nie zwracaj?ce warto?ci.
		// W?a?ciwie powinno si? przetwarza? przekazywany task, ale tutaj nie jest to robione.
		//
		// ScheduledFuture<?> scheduleFuture = executorService.scheduleAtFixedRate(

		executorService.scheduleAtFixedRate(
				()->{
					System.out.println("running"); // do debbugowania
		            ai.incrementAndGet();
		            sl.statusChanged(new Status(taskId,ai.get()));
				}, 
				1, 10, TimeUnit.MILLISECONDS);	
		
		// Powy?sze mo?na by?oby zrobi? w p?tli 
		//  for(int i=1; i<=100; i++){
		//     try {
		//         ai.incrementAndGet();
		//        Thread.sleep(1000);
		//     } catch(InterruptedException e){System.out.println(e);}
        //     sl.statusChanged(new Status(taskId,ai.get()));
		// ale zrobiono to inaczej. 
		
		// Poniewa? zapuszczono egzekutor, trzeba poczeka?, a? przekr?ci 
		// si? w nim 100 zada?, po czym nale?y zamkn?? serwis egzekutora 
		// i zako?czy dzia?anie samego egzekutora.
		// Mo?na to zrobi? "na zewn?trz", w kolejnym egzekutorze.
		// Przyk?ad ten nie jest mo?e najlepszy, ale chodzi?o w nim 
		// o pokazanie synchronizacji przez zmienn? wsp?dzielon?.

		ExecutorService executor = Executors.newSingleThreadExecutor();
		// uruchom zadanie, kt?re sko?czy si?, gdy licznik przekroczy warto?? 100
		executor.submit(() -> {
		      while (true) {
		          //System.out.println(scheduleFuture.isDone()); will always print false
		          try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		          if (ai.get() >= 100) { 
		        	  // przekr?cili?my 100 razy, 
		        	  // mo?emy zwr?ci? wynik przetwarzania
		        	 // tutaj "przetwarzanie" polega na zamianie literek na du?e
		        	  result = task.toUpperCase(); 
		              System.out.println("finished");
		              //scheduleFuture.cancel(true);
		              executorService.shutdown();
		              executor.shutdown();
		              break;
		          }
		      }			
			});
		
		// Je?li chcieliby?my zaznaczy?, ?e co? posz?o nie tak 
		// nale?a?oby zwr?ci? false
		// (np. gdy ostatnie przetwarzanie jeszcze nie dobieg?o ko?ca)
		// 
		// Je?li wszystko posz?o ok, wtedy zwracane jest true
	    //
		// Uwaga: przypominaj?c - implementacja "przetwarzania" jest asynchroniczna,
		//        zwr?cenie warto?ci true nale?y interpretowa? jako informacj? o tym,
		//        ?e zlecenie przetwarzania si? uda?o.
		return true;
	}

	@Override
	public String getInfo() {
		return "Zamiana literek na du?e";
	}

	@Override
	public String getResult() {
		return result;
	}

}
