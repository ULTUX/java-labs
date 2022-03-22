package pl.edu.pwr.quizapp;

import com.jayway.jsonpath.JsonPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://api.teleport.org/api/continents/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        System.out.println(con.getResponseCode());
        BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));




//        String string = JsonPath.parse(input.readLine()).read("$['_embedded']['city:search-results'][0]['_links']['city:item']['href']",String.class);
//        System.out.println(string);

        ResourceBundle enMessages = ResourceBundle.getBundle("DefaultBundle", new Locale("en", "US"));
        System.out.println(enMessages.getString("greeting"));
        System.out.println(enMessages.getString("salute"));

        ResourceBundle plMessages = ResourceBundle.getBundle("DefaultBundle", new Locale("pl", "PL"));
        System.out.println(plMessages.getString("greeting"));
        System.out.println(plMessages.getString("salute"));

        MessageFormat format2 = new MessageFormat(plMessages.getString("pattern2"));
        format2.setLocale(new Locale("pl", "PL"));
        Object[] messageArgs = {null, "XDisk"};

        for (int i = 0; i < 10; i++){
            messageArgs[0] = i;
            System.out.println(format2.format(messageArgs));
        }



    }
}
