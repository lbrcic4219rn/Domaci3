package main.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

    public static final int TCP_PORT = 8113;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final int POOL_SIZE = 10;

    public static void main(String[] args) {
        try (var serverSocket = new ServerSocket(TCP_PORT);
             var pool = Executors.newFixedThreadPool(POOL_SIZE)) {

            LOGGER.info("Server listening on port " + TCP_PORT);
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                pool.submit(new ServerThread(socket));
            }
        } catch (IOException e) {
            LOGGER.severe("Error occurred while starting server: " + e.getMessage());
        }

    }


}
