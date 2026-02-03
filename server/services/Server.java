package se.mau.grupp7.server.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Server that listens for incoming connections
 * Handles each connection with a new thread
 * <p>
 * Created by: Christopher O'Driscoll
 * Updated by: Frida Jacobsson 2021-05-21
 */
public class Server implements Runnable {

    private ServerSocket serverSocket;
    private final Thread serverThread = new Thread(this);
    private boolean serverRunning;
    private ExecutorService executor;

    /**
     * Simplified constructor
     *
     * @param port port to be used
     */
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverRunning = true;
            serverThread.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * starts a new thread for each incoming connection from a client
     */
    @Override
    public void run() {
    }
}
