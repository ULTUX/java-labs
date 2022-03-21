package pl.edu.pwr.quizapp;

import com.jayway.jsonpath.JsonPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONDataParser {
    private final String apiURL;
    private final String dataPath;
    private String value = null;

    public JSONDataParser(String apiURL, String jsonAnswerPath) {
        this.apiURL = apiURL;
        this.dataPath = jsonAnswerPath;
    }

    public String getValue() throws IOException {
        if (value == null) fetchJsonData();
        return value;
    }

    private void fetchJsonData() throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if (con.getResponseCode() != 200) throw new IOException("Invalid URL provided");
        BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
        this.value = JsonPath.parse(input.readLine()).read(dataPath, String.class);
    }
}