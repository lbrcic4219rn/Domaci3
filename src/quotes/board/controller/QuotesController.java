package quotes.board.controller;

import http.Request;
import http.response.HtmlResponse;
import http.response.RedirectResponse;
import http.response.Response;
import quotes.Quote;
import quotes.board.QuoteOfTheDayClient;
import quotes.board.QuotesStorage;

import java.util.List;

public class QuotesController extends Controller {

    public QuotesController(Request request) {
        super(request);
    }

    @Override
    public Response doGet() {
        Quote quote = new QuoteOfTheDayClient().getQuoteOfTheDay();
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

        if (quote != null) {
            html.append(renderQuote(quote));
        } else {
            html.append("<p><i>Quote of the day is unavailable right now.</i></p>");
        }

        html.append("""
            <hr>

            <h2>Saved quotes</h2>
            """);

        for (Quote saved : quotes) {
            html.append("""
                <div style="margin-bottom: 12px;">
                    <hr>
                """)
                    .append(renderQuote(saved))
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

    private static String renderQuote(Quote quote) {
        return "<p><b>" + escapeHtml(quote.author()) + "</b> - \"" + escapeHtml(quote.text()) + "\"</p>";
    }

    private static String escapeHtml(String value) {
        if (value == null) return "";
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
