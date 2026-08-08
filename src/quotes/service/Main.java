package quotes.service;

import config.Config;
import http.Server;

public class Main {

    public static void main(String[] args) {
        new Server(
                "Quote of the day service",
                Config.getInt("quotes.service.port", 8114),
                Config.getInt("quotes.pool.size", 10),
                new QuoteServiceHandler()
        ).start();
    }
}
