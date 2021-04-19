import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import java.net.HttpURLConnection;

public class PublicClient {

    private final String URL = "https://api.pro.coinbase.com";

    public PublicClient () {
    }

    public JSONArray getProducts () throws IOException, ParseException {
        return (JSONArray) sendRequest("GET", "/products");
    }

    public JSONObject getProductByID (String id) throws IOException, ParseException {
        return (JSONObject) sendRequest("get", "/products/" + id);
    }

    public JSONObject getProductOrderBook (String id, String level) throws IOException, ParseException {
        return (JSONObject) sendRequest("get", "/products/" + id + "?level=" + level);
    }

    public JSONArray getCurrencies () throws IOException, ParseException {
        return (JSONArray) sendRequest("GET", "/currencies");
    }

    public JSONObject getCurrencyByID (String id) throws IOException, ParseException {
        return (JSONObject) sendRequest("get", "/currencies/" + id);
    }

    public JSONObject getProductTicker (String id) throws IOException, ParseException {
        return (JSONObject) sendRequest("get", "/products/" + id + "/ticker");
    }

    /*
    public JSONArray getTrades (String id) {
        //todo: create paginated request & implement method
    }
    */

    public JSONArray getHistoricRates (String id, String start, String end, String granularity) throws IOException, ParseException {
        return (JSONArray) sendRequest("get", "/products/" + id + "?start=" + start + "&end=" + end + "&granularity=" + granularity);
    }

    public JSONObject get24hrRate (String id) throws IOException, ParseException {
        return (JSONObject) sendRequest("get", "/products/" + id + "/stats");
    }

    public JSONObject getTime () throws IOException, ParseException {
        return (JSONObject) sendRequest("get", "/time");
    }

    public Object sendRequest(String method, String path) throws IOException, ParseException {
        URL url = new URL(this.URL + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method.toUpperCase());

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode() + conn.getResponseMessage());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        String output;
        StringBuilder stringBuilder = new StringBuilder();
        while ((output = br.readLine()) != null) {
            stringBuilder.append(output);
        }

        br.close();
        conn.disconnect();

        JSONParser parser = new JSONParser();

        return parser.parse(stringBuilder.toString());
    }
}
