package quotes.service;

import com.google.gson.Gson;
import http.HttpMethod;
import http.Request;
import http.RequestHandler;
import http.response.JsonResponse;
import http.response.NotFoundResponse;
import http.response.Response;
import quotes.Quote;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class QuoteServiceHandler implements RequestHandler {

    private final Gson gson = new Gson();

    @Override
    public Response handle(Request request) {
        if (request.httpMethod() == HttpMethod.GET && "/".equals(request.path())) {
            return new JsonResponse(gson.toJson(randomQuote()));
        }
        return new NotFoundResponse("Not found: " + request.path());
    }

    private static Quote randomQuote() {
        List<Quote> quotes = QuoteStorage.getInstance().getQuoteList();
        return quotes.get(ThreadLocalRandom.current().nextInt(quotes.size()));
    }
}
