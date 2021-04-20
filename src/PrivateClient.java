import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class PrivateClient {

    private final String SECRET_KEY;
    private final String PUBLIC_KEY;
    private final String PASSPHRASE;
    private final String URL = "https://api.pro.coinbase.com";

    public PrivateClient (String secretKey, String publicKey, String passphrase) {
        this.SECRET_KEY = secretKey;
        this.PUBLIC_KEY = publicKey;
        this.PASSPHRASE = passphrase;
    }

    public Object sendGetRequest(String endpoint) throws NoSuchAlgorithmException, InvalidKeyException, IOException, ParseException {
        String timestamp = String.valueOf(getTimestamp());
        String requestMethod = "GET";
        String signature = new Signature(SECRET_KEY).generate(endpoint,requestMethod, "", timestamp);

        URL url = new URL(URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("content-type", "application/json");
        conn.setRequestProperty("CB-ACCESS-KEY", PUBLIC_KEY);
        conn.setRequestProperty("CB-ACCESS-SIGN", signature);
        conn.setRequestProperty("CB-ACCESS-TIMESTAMP", timestamp);
        conn.setRequestProperty("CB-ACCESS-PASSPHRASE", PASSPHRASE);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode() + conn.getResponseMessage());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String output;
        StringBuilder stringBuffer = new StringBuilder();
        while ((output = br.readLine()) != null) {
            stringBuffer.append(output);
        }

        br.close();
        conn.disconnect();

        JSONParser parser = new JSONParser();

        return parser.parse(stringBuffer.toString());
    }

    public Object sendPostRequest () {
        return null;
    }

    private long getTimestamp () {
        return Instant.now().getEpochSecond();
    }
}
