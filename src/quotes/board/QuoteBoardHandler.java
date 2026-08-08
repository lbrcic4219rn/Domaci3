package quotes.board;

import http.HttpMethod;
import http.Request;
import http.RequestHandler;
import http.response.NotFoundResponse;
import http.response.Response;
import quotes.board.controller.QuotesController;

public class QuoteBoardHandler implements RequestHandler {

    @Override
    public Response handle(Request request) {
        if (request.path().equals("/quotes") && request.httpMethod() == HttpMethod.GET) {
            return new QuotesController(request).doGet();
        } else if (request.path().equals("/save-quote") && request.httpMethod() == HttpMethod.POST) {
            return new QuotesController(request).doPost();
        }

        return new NotFoundResponse("Page: " + request.path() + ". Method: " + request.httpMethod() + " not found!");
    }
}
