package se.mau.grupp7.server.services;

import se.mau.grupp7.happyplant2.model.Plant;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class responsible for calling the database about a users library.
 * Created by: Linn Borgström
 * Updated by: Frida Jacobsson 2021-05-21
 */
public class UserPlantRepository {

    private PlantRepository plantRepository;
    private IQueryExecutor database;

    /**
     * Constructor that creates a connection to the database.
     *
     * @throws SQLException
     * @throws UnknownHostException
     */
    public UserPlantRepository(PlantRepository plantRepository, IQueryExecutor database) throws UnknownHostException, SQLException {
        this.plantRepository = plantRepository;
        this.database = database;

    }

    public boolean savePlant(Plant plant) {
        return false;
    }
    public ArrayList<Plant> getUserLibrary() {
        return null;
    }

    public Plant getPlant(String nickname) {
        return null;
    }

    public boolean deletePlant(String nickname) {
        return false;
    }

    public boolean changeLastWatered(String nickname, LocalDate date) {
        return false;
    }

    public boolean changeNickname(String nickname, String newNickname) {
        return false;
    }

    public boolean changeAllToWatered() {
        return false;
    }

    public boolean changePlantPicture(Plant plant) {
        return false;
    }
}