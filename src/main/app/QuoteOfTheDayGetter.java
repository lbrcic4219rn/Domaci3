package main.app;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuoteOfTheDayGetter {
    private final Gson gson = new Gson();
    public static final String HOST = "localhost";
    public static final int PORT = 8114;
    private static final Logger LOGGER = Logger.getLogger(QuoteOfTheDayGetter.class.getName());

    public Quote getQuoteOfTheDay() {
        try (var socket = new Socket(HOST, PORT);
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            out.printf("GET / HTTP/1.1\r\nHost: %s:%d\r\n\r\n", HOST, PORT);

            String reqLine = in.readLine();
            do {
                System.out.println(reqLine);
                reqLine = in.readLine();
            } while (!reqLine.trim().isEmpty());

            String quoteJson = in.readLine();
            System.out.println(quoteJson);

            return gson.fromJson(quoteJson, Quote.class);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching quote of the day", e);
        }

        return null;
    }
}
