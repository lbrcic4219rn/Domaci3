package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class Server {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final String name;
    private final int port;
    private final int poolSize;
    private final RequestHandler handler;

    public Server(String name, int port, int poolSize, RequestHandler handler) {
        this.name = name;
        this.port = port;
        this.poolSize = poolSize;
        this.handler = handler;
    }

    public void start() {
        try (var serverSocket = new ServerSocket(port);
             var pool = Executors.newFixedThreadPool(poolSize)) {

            LOGGER.info(name + " listening on port " + port);
            while (!Thread.currentThread().isInterrupted()) {
                pool.submit(new ServerThread(serverSocket.accept(), handler));
            }
        } catch (IOException e) {
            LOGGER.severe("Error occurred while running " + name + ": " + e.getMessage());
        }
    }
}
