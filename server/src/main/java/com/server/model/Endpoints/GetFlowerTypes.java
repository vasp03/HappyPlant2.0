package com.server.model.Endpoints;

import com.server.controller.Controller;
import com.server.model.APIEndpoint;

import io.javalin.http.Context;

public class GetFlowerTypes implements APIEndpoint {
    private Controller controller;

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public String path() {
        return "/api/v1/getFlowerTypes";
    }

    @Override
    public void handle(Context context) throws UnsupportedOperationException {
        // For demonstration purposes, returning a static list of flower types
        String[] flowerTypes = { "Rose", "Tulip", "Daisy", "Sunflower", "Lily" };

        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{ \"flowerTypes\": [");
        for (int i = 0; i < flowerTypes.length; i++) {
            jsonResponse.append("\"").append(flowerTypes[i]).append("\"");
            if (i < flowerTypes.length - 1) {
                jsonResponse.append(", ");
            }
        }
        jsonResponse.append("] }");

        context.status(200);
        context.result(jsonResponse.toString());
    }
}