package main.app;

import main.app.controller.QuotesController;
import main.http.HttpMethod;
import main.http.Request;
import main.http.response.Response;

public class RequestHandler {
    public Response handle(Request request) throws Exception {
        if (request.path().equals("/quotes") && request.httpMethod().equals(HttpMethod.GET)) {
            return (new QuotesController(request)).doGet();
        } else if (request.path().equals("/save-quote") && request.httpMethod().equals(HttpMethod.POST)) {
            return (new QuotesController(request)).doPost();
        }

        throw new Exception("Page: " + request.path() + ". Method: " + request.httpMethod() + " not found!");
    }
}
