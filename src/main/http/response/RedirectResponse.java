package main.http.response;

public class RedirectResponse extends Response {
    private final String location;

    public RedirectResponse(String location) {
        this.location = location;
    }

    @Override public int getStatusCode() {
        return 303;
    }
    @Override public String getContentType() {
        return "text/html; charset=UTF-8";
    }
    @Override public String getBody() {
        return "";
    }

    @Override
    public String getResponseString() {
        return "HTTP/1.1 303 See Other\r\n"
                + "Location: " + location + "\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
    }
}
