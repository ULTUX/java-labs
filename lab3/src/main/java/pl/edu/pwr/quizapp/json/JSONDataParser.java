package pl.edu.pwr.quizapp.json;

import com.jayway.jsonpath.JsonPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

public class JSONDataParser {
    private final String apiURL;
    private final String dataPath;
    private String value = null;

    public JSONDataParser(String apiURL, String jsonPath) {
        this.apiURL = apiURL;
        this.dataPath = jsonPath;
    }

    public String getValue() throws IOException {
        if (value == null) fetchJsonData();
        return value;
    }

    private void fetchJsonData() throws IOException {
        var input = getResponse();
        this.value = JsonPath.parse(input).read(dataPath, String.class);
    }

    public String getResponse() throws IOException {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if (con.getResponseCode() != 200) throw new IOException("Invalid URL provided");
        BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return input.readLine();
    }


    public List<String> getValueList(String contextPath) throws IOException {
        var context = JsonPath.parse(getResponse());
        return context.read(dataPath + "[*]['" + contextPath + "']");
    }

    public List<LinkedHashMap<String, String>> getMapList() throws IOException {
        var context = JsonPath.parse(getResponse());
        return context.read(dataPath + "[*]");
    }

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
