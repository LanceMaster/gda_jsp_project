package utils;

import com.google.gson.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class IamportUtils {
    private static final String API_KEY = "6420630467535422";
    private static final String API_SECRET = "Gpx96Zg6nPIUNmkbBtBdpI8mTEtV6MCyHFHckXTDmEaqsGSVuCmBDzYy1wi63YFrwK82gyFb9ke2MDq0";

    public static boolean verifyPayment(String impUid, int expectedAmount) {
        try {
            String token = getAccessToken();
            URL url = new URL("https://api.iamport.kr/payments/" + impUid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", token);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();

            int paidAmount = json.getAsJsonObject("response").get("amount").getAsInt();

            return paidAmount == expectedAmount;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getAccessToken() throws IOException {
        URL url = new URL("https://api.iamport.kr/users/getToken");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonBody = "{\"imp_key\":\"" + API_KEY + "\", \"imp_secret\":\"" + API_SECRET + "\"}";
        conn.getOutputStream().write(jsonBody.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = reader.readLine();
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        return json.getAsJsonObject("response").get("access_token").getAsString();
    }
}
