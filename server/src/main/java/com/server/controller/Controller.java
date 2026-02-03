package com.server.controller;

import com.server.HappyPlant2;

public class Controller {
    private Database database;
    private APIRunner apiRunner;
    private HappyPlant2 serverInstance;

    public Controller(HappyPlant2 serverInstance) {
        this.serverInstance = serverInstance;
        this.database = new Database(this);
        this.apiRunner = new APIRunner(this);
    }

    public Database getDatabase() {
        return this.database;
    }

    public APIRunner getAPIRunner() {
        return this.apiRunner;
    }

    public HappyPlant2 getServerInstance() {
        return this.serverInstance;
    }
}
