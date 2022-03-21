import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://api.teleport.org/api/continents/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        System.out.println(con.getResponseCode());
        BufferedReader input = new BufferedReader(new InputStreamReader(con.getInputStream()));

        Object obj = JSONValue.parse(input.readLine());
        JSONObject json = (JSONObject) obj;
        JSONArray array = (JSONArray) ((JSONObject)json.get("_links")).get("continent:items");
        for (Object object: array){
            JSONObject jsonObject = (JSONObject) object;
            System.out.println(jsonObject.get("name"));
        }



    }
}
