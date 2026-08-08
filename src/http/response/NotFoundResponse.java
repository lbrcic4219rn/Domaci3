package http.response;

public class NotFoundResponse extends PlainTextResponse {

    public NotFoundResponse(String message) {
        super(404, message);
    }
}
