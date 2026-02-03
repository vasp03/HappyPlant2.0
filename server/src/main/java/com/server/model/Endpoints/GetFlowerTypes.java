package com.server.model.Endpoints;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

import com.server.controller.Controller;
import com.server.model.APIEndpoint;

import io.javalin.http.Context;

public class GetFlowerTypes implements APIEndpoint {
    private final Controller controller;

    public GetFlowerTypes(Controller controller) {
        this.controller = controller;
    }

    @Override
    public String path() {
        return "/api/v1/getFlowerTypes";
    }

    @Override
    public void handle(Context context) throws UnsupportedOperationException {
        String key = controller.getServerInstance().getEnv().get("PERENUAL_API_KEY");

        StringBuilder apiUrlBuilder = new StringBuilder("https://perenual.com/api/v2/species-list?key=" + key);

        if (context.queryParam("q") != null) {
            apiUrlBuilder.append("&q=").append(context.queryParam("q"));
        }

        try {
            URI uri = new URI(apiUrlBuilder.toString());
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                context.status(responseCode).result("{\"error\":\"Failed to fetch flower types.\"}");
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            context.status(200).result(response.toString());
        } catch (Exception e) {
            context.status(500).result("{\"error\":\"Failed to fetch flower types.\"}");
        }
    }
}