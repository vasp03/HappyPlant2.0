package se.mau.grupp7.happyplant2.controller;

public class ServerConnection {
    private final BackendConnector backendConnector;
    private final String SERVER_ADDRESS = "localhost";

    public ServerConnection(BackendConnector backendConnector){
        this.backendConnector = backendConnector;
    }
}
