package main.app;

import java.util.concurrent.CopyOnWriteArrayList;

public class QuotesStorage {
    private static final QuotesStorage instance = new QuotesStorage();
    private final CopyOnWriteArrayList<Quote> quotes = new CopyOnWriteArrayList<>();

    private QuotesStorage() { }

    public static QuotesStorage getInstance() {
        return instance;
    }

    public CopyOnWriteArrayList<Quote> getQuotes() {
        return quotes;
    }
}
