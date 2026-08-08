package http.response;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class Response {

    public abstract int getStatusCode();

    public abstract String getContentType();

    public abstract String getBody();

    protected Map<String, String> getExtraHeaders() {
        return Map.of();
    }

    public String getResponseString() {
        StringBuilder response = new StringBuilder()
                .append("HTTP/1.1 ").append(getStatusCode()).append(' ').append(getReasonPhrase()).append("\r\n")
                .append("Content-Type: ").append(getContentType()).append("\r\n")
                .append("Content-Length: ").append(getBody().getBytes(StandardCharsets.UTF_8).length).append("\r\n");

        getExtraHeaders().forEach((name, value) -> response.append(name).append(": ").append(value).append("\r\n"));

        return response.append("\r\n").append(getBody()).toString();
    }

    private String getReasonPhrase() {
        return switch (getStatusCode()) {
            case 200 -> "OK";
            case 303 -> "See Other";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            case 501 -> "Not Implemented";
            default -> "Unknown";
        };
    }
}
