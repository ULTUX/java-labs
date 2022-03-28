package pl.edu.pwr.lab4.processing;
public interface StatusListener {
	/**
	 * Metoda s�uchacza
	 * @param s - status przetwarzania zadania
	 */
	void statusChanged(Status s);
}
