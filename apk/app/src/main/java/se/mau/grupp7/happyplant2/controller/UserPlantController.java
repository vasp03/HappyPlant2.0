package se.mau.grupp7.happyplant2.controller;

import java.util.ArrayList;
import java.util.Date;

import se.mau.grupp7.happyplant2.model.UserPlant;

public class UserPlantController {
    private final BackendConnector backendConnector;

    public UserPlantController(BackendConnector backendConnector) {
        this.backendConnector = backendConnector;
    }

    public ArrayList<UserPlant> GetUserPlants() {
        return backendConnector.getServerConnection().GetUserPlants();
    }

    public UserPlant AddUserPlant(UserPlant userPlant) {
        return backendConnector.getServerConnection().AddUserPlant(userPlant);
    }

    public UserPlant UpdateUserPlant(UserPlant userPlant) {
        return backendConnector.getServerConnection().UpdateUserPlant(userPlant);
    }

    public boolean DeleteUserPlant(UserPlant userPlant) {
        return backendConnector.getServerConnection().DeleteUserPlant(userPlant);
    }

    public boolean WaterUserPlant(UserPlant userPlant) {
        UserPlant wateredPlant = new UserPlant(
                userPlant.getName(),
                userPlant.getDescription(),
                userPlant.getImageURL(),
                userPlant.getWateringInterval(),
                userPlant.getWateringAmount(),
                new Date()
        );

        UserPlant updatedPlant = backendConnector.getServerConnection().UpdateUserPlant(userPlant);

        return updatedPlant != null;
    }
}
