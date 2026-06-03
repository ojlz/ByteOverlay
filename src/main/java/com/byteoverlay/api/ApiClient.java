package com.byteoverlay.api;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    private static final String API_URL = "https://mush.com.br/api/player/";
    private final Gson gson = new Gson();

    public MushResponse fetchPlayer(String nick) throws Exception {
        URL url = new URL(API_URL + nick);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();
        
        if (responseCode == 429) {
            throw new Exception("Rate Limit (429)");
        }

        if (responseCode == 404) {
             MushResponse errorResponse = new MushResponse();
             errorResponse.success = false;
             errorResponse.errorCode = 404;
             return errorResponse;
        }

        if (responseCode != 200) {
            throw new Exception("HTTP error code: " + responseCode);
        }

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            return gson.fromJson(reader, MushResponse.class);
        }
    }
}
