package se.mau.grupp7.happyplant2.controller;

import java.util.ArrayList;
import java.util.Date;

import se.mau.grupp7.happyplant2.model.PlantDetails;
import se.mau.grupp7.happyplant2.model.PlantType;
import se.mau.grupp7.happyplant2.model.UserPlant;
import se.mau.grupp7.happyplant2.model.WaterAmount;

public class ServerConnection {
    private final BackendConnector backendConnector;
    private final String SERVER_ADDRESS = "localhost";

    public ServerConnection(BackendConnector backendConnector){
        this.backendConnector = backendConnector;
    }

    public ArrayList<PlantType> GetAllPlantTypes() {
        ArrayList<PlantType> plantTypes = new ArrayList<PlantType>();

        plantTypes.add(new PlantType("Flower 1", "This is a flower", "https://example.com/flower1.jpg"));
        plantTypes.add(new PlantType("Flower 2", "This is another flower", "https://example.com/flower2.jpg"));
        plantTypes.add(new PlantType("Flower 3", "This is a third flower", "https://example.com/flower3.jpg"));

        return plantTypes;
    }

    public ArrayList<UserPlant> GetUserPlants() {
        ArrayList<UserPlant> plantTypes = new ArrayList<UserPlant>();

        plantTypes.add(new UserPlant("User PlantFlower 1", "This is a flower", "https://example.com/flower1.jpg", 7, WaterAmount.RARELY, new Date()));
        plantTypes.add(new UserPlant("User Flower 2", "This is another flower", "https://example.com/flower2.jpg", 7, WaterAmount.RARELY, new Date()));
        plantTypes.add(new UserPlant("User Flower 3", "This is a third flower", "https://example.com/flower3.jpg", 7, WaterAmount.RARELY, new Date()));

        return plantTypes;
    }

    public UserPlant AddUserPlant(PlantDetails plant) {
        return null;
    }

    public UserPlant UpdateUserPlant(UserPlant userPlant) {
        return null;
    }

    public boolean DeleteUserPlant(UserPlant userPlant) {
        return false;
    }
}
