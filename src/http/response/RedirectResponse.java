package http.response;

import java.util.Map;

public class RedirectResponse extends Response {

    private final String location;

    public RedirectResponse(String location) {
        this.location = location;
    }

    @Override
    public int getStatusCode() {
        return 303;
    }

    @Override
    public String getContentType() {
        return "text/html; charset=UTF-8";
    }

    @Override
    public String getBody() {
        return "";
    }

    @Override
    protected Map<String, String> getExtraHeaders() {
        return Map.of("Location", location);
    }
}
