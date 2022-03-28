package pl.edu.pwr.lab4.example;

import pl.edu.pwr.lab4.processing.StatusListener;
import pl.edu.pwr.lab4.processors.MyProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

	public static void main(String[] args) {
		// Niech zmienna p reprezentuje referencj? do jakiej? nieznanej klasy, 
	    // implementuj?cej interfejs Processor, za?adowanej w?asnym ?adowaczem.
		// Jednak w tym przyk?adowym kodzie nie dostarczono ?adnego takiego ?adowacza klas.
		// Zamiast tego do p przypisano warto?? wy?uskana bezpo?rednio z klasy MyProcessor
		// (implementuj?cej interfejs Processor, ale znanej na etapie kompilacji projektu).
		// W ten spos?b "zasymulowano" ?adowanie klasy.
		Class<?> p = MyProcessor.class;
		// Maj?c za?adowan? klas? mo?na si?gn?? do jej metod i je wywo?a?. 
		try {
			// Pozyskujemy konstruktor
			Constructor<?> cp = p.getConstructor();
			// a nast?pnie tworzymy obiekt za?adowanej klasy 
			// (trzeba to zrobi?, aby mo?na by?o wywo?a? metody instancyjne).
			Object o = cp.newInstance();
			
			
			
			// Poniewa? wiemy, ?e za?adowana klasa implementuje interfejs processing.Processor, 
			// dlatego szukamy w tej klasie znanych metod.
			// Najpierw dowiadujemy si?, na czym polega algorytm przetwarzania wywo?uj?c getInfo()
			Method getInfoMethod = p.getDeclaredMethod("getInfo");
			System.out.println((String)getInfoMethod.invoke(o));

			// Nast?pnie przesy?amy zadanie do wykonania metod? submitTask()
			// Method method = p.getDeclaredMethod("submitTask", new Class[] {String.class,
			// StatusListener.class});					
			Method submitTaskMethod = p.getDeclaredMethod("submitTask", String.class, StatusListener.class);
			
			// Aby wywo?a? t? metod? nale?y pos?u?y? si? odpowiednimi parametrami: task, sl
			// gdzie sl referencja do w?asnego s?uchacza - instancji klasy implementuj?cej interfejs StatusListener.			
              
			// boolean b = (boolean) method.invoke(o,new Object[] {"Text to process", new
			// MyStatusListener()});
			boolean b = (boolean) submitTaskMethod.invoke(o, "Tekst na wej?cie", new MyStatusListener());

			// Implementacja metody submitTask w klasie MyProcessor jest asynchroniczna.
			// Do rozpoznania ko?ca przetwarzania i pobrania wyliczonego wyniku 
			// nale?y wykorzysta? metody s?uchacza.
			// Na razie piszemy tylko komunikat, ?e zainicjowano przetwarzanie
			if (b)
				System.out.println("Processing started correctly");
			else
				System.out.println("Processing ended with an error");

			// Do rozpoznania ko?ca przetwarzania s?u?y metoda statusChanged
			// s?uchacza MyStatusListener. Do tej metody dostarczana b?dzie instancja Status,
			// z kt?rej wyci?gn?? mo?na progress i taskId.
			// Jak progress dojdzie do ko?ca, b?dzie mo?na pobra? wynik przetwarzania
			// getod? getResult() z instancji klasy MyProcessor.
			// Je?li z metody getResult() otrzyma si? co? innego ni? null, to w?a?nie b?dzie to
            // wynik

			// Poniewa? mamy program konsolowy, asynchroniczno?? wymaga uruchamieniam w?tku
			// oczekuj?cego na rezultat (sprawdzaj?cy, czy przetwarzanie nie dobieg?o ko?ca).
			// W aplikacji okienkowej podobnie zadzia?a?by s?uchacz, kt?ry 
			// odpali?by pobranie rezultatu.

			
			Method getResultMethod = p.getDeclaredMethod("getResult");

			ExecutorService executor = Executors.newSingleThreadExecutor();
			
			// uruchom zadanie, kt?re sko?czy si?, gdy result!=null
			executor.submit(() -> {
				String result = null;				
				while (true) {
					// System.out.println(scheduleFuture.isDone());
					
						try {
							Thread.sleep(800);

						// String result = (String) getResultMethod.invoke(o,new Object[] {});
							result = (String) getResultMethod.invoke(o);
						} catch (InterruptedException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
						if (result != null) {
							System.out.println("Result: " + result);
							executor.shutdown();
							break;
						}
				}
			});

			System.out.println("main FINISHED");

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
