package http.response;

public class PlainTextResponse extends Response {

    private final int statusCode;
    private final String body;

    public PlainTextResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getContentType() {
        return "text/plain; charset=UTF-8";
    }

    @Override
    public String getBody() {
        return body;
    }
}
