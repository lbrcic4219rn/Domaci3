package http;

import http.response.Response;


@FunctionalInterface
public interface RequestHandler {
    Response handle(Request request);
}
