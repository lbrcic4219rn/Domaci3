package quotes.board;

import com.google.gson.Gson;
import config.Config;
import quotes.Quote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuoteOfTheDayClient {

    public static final String HOST = Config.get("quotes.service.host", "localhost");
    public static final int PORT = Config.getInt("quotes.service.port", 8114);
    public static final int TIMEOUT_MS = Config.getInt("quotes.service.timeout.ms", 2000);

    private static final Logger LOGGER = Logger.getLogger(QuoteOfTheDayClient.class.getName());

    private final Gson gson = new Gson();

    public Quote getQuoteOfTheDay() {
        try (var socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PORT), TIMEOUT_MS);
            socket.setSoTimeout(TIMEOUT_MS);

            try (var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 var out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

                out.printf("GET / HTTP/1.1\r\nHost: %s:%d\r\n\r\n", HOST, PORT);

                skipHeaders(in);
                return gson.fromJson(in.readLine(), Quote.class);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while fetching quote of the day", e);
        }
        return null;
    }

    private static void skipHeaders(BufferedReader in) throws IOException {
        String line = in.readLine();
        while (line != null && !line.trim().isEmpty()) {
            line = in.readLine();
        }
    }
}
