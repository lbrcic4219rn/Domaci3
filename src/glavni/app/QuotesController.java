package glavni.app;

import glavni.http.Request;
import glavni.http.response.HtmlResponse;
import glavni.http.response.RedirectResponse;
import glavni.http.response.Response;

import java.util.Iterator;

public class QuotesController extends Controller {

    public QuotesController(Request request) {
        super(request);
    }

    @Override
    public Response doGet() {
        Quote quote = (new QuoteOfTheDayGetter()).getQuoteOfTheDay();
        StringBuilder htmlBody = new StringBuilder();
        htmlBody.append(
            "<form action=\"/save-quote\" method=\"POST\">" +
            "<label>Author: </label><input name=\"author\" type=\"text\"><br>" +
            "<label>Quote: </label><input name=\"quote\" type=\"text\"><br><br>" +
            "<button>Save Quote</button>" +
            "</form>" +
            "<br>" +
            "<h2>Quote of the day:</h2>\n" +
            "<p><b>" + quote.getAuthor() + "</b> - \"" + quote.getText() + "\"</p>\n" +
            "<h2>Saved quotes:</h2>"
        );
        System.out.println("STORAGE" + QuotesStorage.getInstance().getQuotes());
        Iterator iterator = QuotesStorage.getInstance().getQuotes().iterator();
        while(iterator.hasNext()){
            Quote curr = (Quote)iterator.next();
            System.out.println("element " + curr);
            System.out.println("AUTOR " + curr.getAuthor());
            System.out.println("TEXT " + curr.getText());


            htmlBody.append(
                "<p>----------------------------------------</p>\n" +
                "<p><b>" + curr.getAuthor() + "</b> - \"" + curr.getText() + "\"</p>\n" +
                "<p>----------------------------------------</p>\n"
            );
        }

        return new HtmlResponse(htmlBody.toString());
    }

    @Override
    public Response doPost() {
        QuotesStorage.getInstance().getQuotes().add(
                new Quote(
                        request.getPostParams().get("author"),
                        request.getPostParams().get("quote")
                )
        );
        return new RedirectResponse();
    }
}
