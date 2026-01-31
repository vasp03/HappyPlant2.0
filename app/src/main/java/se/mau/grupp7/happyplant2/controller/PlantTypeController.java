package se.mau.grupp7.happyplant2.controller;

import java.util.ArrayList;

import se.mau.grupp7.happyplant2.model.PlantType;

public class PlantTypeController {
    private final BackendConnector backendConnector;

    public PlantTypeController(BackendConnector backendConnector){
        this.backendConnector = backendConnector;
    }

    /**
     * Supposed to return a list of plant types retrieved from an external API.
     * @return A list of plant types
     */
    public ArrayList<PlantType> GetPlantTypes() {
        return backendConnector.getServerConnection().GetAllPlantTypes();
    }
}
