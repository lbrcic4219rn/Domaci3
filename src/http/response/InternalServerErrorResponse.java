package http.response;

public class InternalServerErrorResponse extends PlainTextResponse {

    public InternalServerErrorResponse(String message) {
        super(500, message);
    }
}
