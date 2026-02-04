package com.server.model.Endpoints;

import java.net.URL;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.controller.CacheDatabase;
import com.server.controller.Controller;
import com.server.model.APIEndpoint;
import com.server.model.FlowerTypes;

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
        String searchQuery = context.queryParam("q");

        if (searchQuery == null || searchQuery.isBlank()) {
            context.status(400).result("{\"error\":\"Query parameter 'q' is required.\"}");
            return;
        }

        CacheDatabase database = controller.getCacheDatabase();

        ArrayList<FlowerTypes> flowerTypes = database.getFlowerTypesSearchResult(searchQuery);

        if (flowerTypes != null && !flowerTypes.isEmpty()) {
            System.out.println("Cache hit for flower types with query: " + searchQuery);
            context.status(200).json(flowerTypes);
            return;
        }

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

            ArrayList<FlowerTypes> fetchedFlowerTypes = new ArrayList<>();

            while ((inputLine = in.readLine()) != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(inputLine);
                JsonNode dataArray = rootNode.path("data");

                if (dataArray.isArray()) {
                    for (JsonNode flowerNode : dataArray) {
                        FlowerTypes flowerType = new FlowerTypes(
                                flowerNode.path("id").asInt(),
                                flowerNode.path("common_name").asText(),
                                objectMapper.convertValue(flowerNode.path("scientific_name"), String[].class),
                                objectMapper.convertValue(flowerNode.path("other_names"), String[].class),
                                flowerNode.path("family").asText(),
                                flowerNode.path("hybrid").asText(),
                                flowerNode.path("authority").asText(),
                                flowerNode.path("subspecies").asText(),
                                flowerNode.path("cultivar").asText(),
                                flowerNode.path("variety").asText(),
                                flowerNode.path("species_epithet").asText(),
                                flowerNode.path("genus").asText(),
                                flowerNode.path("links").path("plant").asText(),
                                flowerNode.path("default_image").path("regular_url").asText(),
                                flowerNode.path("default_image").path("medium_url").asText(),
                                flowerNode.path("default_image").path("small_url").asText(),
                                flowerNode.path("default_image").path("thumbnail").asText());
                        fetchedFlowerTypes.add(flowerType);
                    }
                }
            }

            if (!fetchedFlowerTypes.isEmpty()) {
                database.addFlowerTypeSearchResult(searchQuery, fetchedFlowerTypes);
                System.out.println("Cached " + fetchedFlowerTypes.size()
                        + " flower types for search query: " + searchQuery);
            }

            in.close();
            context.status(200).json(fetchedFlowerTypes);
        } catch (Exception e) {
            context.status(500).result("{\"error\":\"Failed to fetch flower types.\"}");
        }
    }
}