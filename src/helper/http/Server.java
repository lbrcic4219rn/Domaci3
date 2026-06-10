package helper.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {
    public static final int TCP_PORT = 8114;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final int POOL_SIZE = 10;

    public static void main(String[] args) {
        try (var ss = new ServerSocket(TCP_PORT);
             var pool = Executors.newFixedThreadPool(POOL_SIZE)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket sock = ss.accept();
                pool.submit(new ServerThread(sock));
            }
        } catch (IOException e) {
            LOGGER.severe("Error occurred while starting the server: " + e.getMessage());
        }

    }
}
