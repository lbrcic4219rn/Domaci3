package quotes.board;

import config.Config;
import http.Server;

public class Main {

    public static void main(String[] args) {
        new Server(
                "Quote board",
                Config.getInt("quotes.board.port", 8113),
                Config.getInt("quotes.pool.size", 10),
                new QuoteBoardHandler()
        ).start();
    }
}
