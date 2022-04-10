package pl.edu.pwr.lab6.libs;

import java.io.Serializable;
import java.time.Duration; // available since JDK 1.8

/**
 * Klasa reprezentująca zamówienie wyświetlania ogłoszenia o zadanej treści
 * przez zadany czas ze wskazaniem na namiastke klienta, przez którą można
 * przesłać informacje o numerze zamówienia w przypadku jego przyjęcia
 *
 * @author tkubik
 *
 */
public class Order implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String advertText;
    public Duration displayPeriod;
    public IClient client;

    public Order(String advertText, Duration displayPeriod, IClient client) {
        this.advertText = advertText;
        this.displayPeriod = displayPeriod;
        this.client = client;
    }

    public Order(String advertText, Duration displayPeriod) {
        this.advertText = advertText;
        this.displayPeriod = displayPeriod;
    }
}