package com.server;

import java.util.ArrayList;

import com.server.controller.Controller;
import com.server.model.APIEndpoint;

public class HappyPlant2 {
    private final String HOST;
    private final int PORT;

    private Controller controller;

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

        registerEndpoints(controller);

        System.out.println("Starting HappyPlant2 server on " + HOST + ":" + PORT + "...");

        controller.getAPIRunner().start();
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
        endpointsGet.add(new com.server.model.Endpoints.GetFlowerTypes());

        controller.getAPIRunner().registerEndpoints(endpointsGet, endpointsPost, endpointsDelete);

        System.out.println("Registered " + endpointsGet.size() + " GET endpoints.");
        System.out.println("Registered " + endpointsPost.size() + " POST endpoints.");
        System.out.println("Registered " + endpointsDelete.size() + " DELETE endpoints.");

        return true;
    }
}