package main.http.response;

import java.nio.charset.StandardCharsets;

public abstract class Response {
    public abstract int getStatusCode();
    public abstract String getContentType();
    public abstract String getBody();

    public String getResponseString() {
        return "HTTP/1.1 " + getStatusCode() + " " + getReasonPhrase() + "\r\n"
                + "Content-Type: " + getContentType() + "\r\n"
                + "Content-Length: " + getBody().getBytes(StandardCharsets.UTF_8).length + "\r\n"
                + "\r\n"
                + getBody();
    }

    private String getReasonPhrase() {
        return switch (getStatusCode()) {
            case 200 -> "OK";
            case 303 -> "See Other";
            case 404 -> "Not Found";
            default  -> "Unknown";
        };
    }
}
