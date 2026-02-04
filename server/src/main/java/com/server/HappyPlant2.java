package com.server;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.server.controller.Controller;
import com.server.model.APIEndpoint;
import com.server.model.Endpoints.GetFlowerTypes;

import io.github.cdimascio.dotenv.Dotenv;

public class HappyPlant2 {
    private final String HOST;
    private final int PORT;

    private Controller controller;

    private final Dotenv env = Dotenv.load();

    public HappyPlant2(String[] args) {
        if (args.length >= 2) {
            System.out.println("Starting server on custom port: " + args[0] + " and host: " + args[1]);
            PORT = Integer.parseInt(args[0]);
            HOST = args[1];
        } else {
            HOST = "localhost";
            PORT = 5000;
        }

        controller = new Controller(this);

        ensureAPIKeys();

        registerEndpoints(controller);

        System.out.println("Starting HappyPlant2 server on " + HOST + ":" + PORT + "...");

        controller.getAPIRunner().start();
    }

    public Dotenv getEnv() {
        return env;
    }

    public void ensureAPIKeys() {
        if (Files.notExists(Paths.get("./.env")))
            throw new RuntimeException(".env environment variable file not found.");
        else if (getEnv().get("PERENUAL_API_KEY") == null || getEnv().get("PERENUAL_API_KEY").isBlank())
            throw new RuntimeException("Perenual API Key missing");
    }

    public String getHOST() {
        return HOST;
    }

    public int getPORT() {
        return PORT;
    }

    /**
     * Register all API endpoints. Ex. GET /plants, POST /users, etc.
     */
    private boolean registerEndpoints(Controller controller) {
        ArrayList<APIEndpoint> endpointsGet = new ArrayList<>();
        ArrayList<APIEndpoint> endpointsPost = new ArrayList<>();
        ArrayList<APIEndpoint> endpointsDelete = new ArrayList<>();

        // GET Endpoints
        endpointsGet.add(new GetFlowerTypes(controller));

        controller.getAPIRunner().registerEndpoints(endpointsGet, endpointsPost, endpointsDelete);

        System.out.println("Registered " + endpointsGet.size() + " GET endpoints.");
        System.out.println("Registered " + endpointsPost.size() + " POST endpoints.");
        System.out.println("Registered " + endpointsDelete.size() + " DELETE endpoints.");

        return true;
    }
}