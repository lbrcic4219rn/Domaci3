package main.app.controller;

import main.app.Quote;
import main.app.QuoteOfTheDayGetter;
import main.app.QuotesStorage;
import main.http.Request;
import main.http.response.HtmlResponse;
import main.http.response.RedirectResponse;
import main.http.response.Response;

import java.util.List;

public class QuotesController extends Controller {

    public QuotesController(Request request) {
        super(request);
    }

    @Override
    public Response doGet() {
        QuoteOfTheDayGetter getter = new QuoteOfTheDayGetter();
        Quote quote = getter.getQuoteOfTheDay();

        List<Quote> quotes = QuotesStorage.getInstance().getQuotes();

        StringBuilder html = new StringBuilder();

        html.append("""
            <form action="/save-quote" method="POST">
                <label>Author:</label>
                <input name="author" type="text">
                <br><br>

                <label>Quote:</label>
                <input name="quote" type="text">
                <br><br>

                <button type="submit">Save Quote</button>
            </form>

            <hr>

            <h2>Quote of the day</h2>
            """);

        html.append("<p><b>")
                .append(quote.author())
                .append("</b> - \"")
                .append(quote.text())
                .append("\"</p>");

        html.append("""
            <hr>

            <h2>Saved quotes</h2>
            """);

        for (Quote curr : quotes) {
            html.append("""
                <div style="margin-bottom: 12px;">
                    <hr>
                """)
                    .append("<p><b>")
                    .append(curr.author())
                    .append("</b> - \"")
                    .append(curr.text())
                    .append("\"</p>")
                    .append("</div>");
        }

        return new HtmlResponse(html.toString());
    }

    @Override
    public Response doPost() {
        QuotesStorage.getInstance().getQuotes().add(
                new Quote(
                        request.postParams().get("author"),
                        request.postParams().get("quote")
                )
        );
        return new RedirectResponse("/quotes");
    }
}
