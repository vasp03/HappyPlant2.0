package se.mau.grupp7.happyplant2.controller;

/**
 * Main controller for backend functionality.
 * Every backend class is supposed to be run through this to frontend.
 */
public class BackendConnector {
    private final ServerConnection serverConnection;
    private final UserPlantController userPlantController;

    public BackendConnector(){
        serverConnection = new ServerConnection(this);
        userPlantController = new UserPlantController(this);

        setup();
    }

    public int setup(){
        // Run setup code for backend functionality here.

        //Return REST status code from here
        return 200;
    }

    public ServerConnection getServerConnection(){
        return serverConnection;
    }

    public UserPlantController getUserPlantController(){
        return userPlantController;
    }
}

