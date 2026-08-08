package http.response;

public class JsonResponse extends Response {

    private final String json;

    public JsonResponse(String json) {
        this.json = json;
    }

    @Override
    public int getStatusCode() {
        return 200;
    }

    @Override
    public String getContentType() {
        return "application/json; charset=UTF-8";
    }

    @Override
    public String getBody() {
        return json;
    }
}
