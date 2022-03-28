package glavni.http;

import java.util.HashMap;

public class Request {

    private final HttpMethod httpMethod;

    private final String path;
    private HashMap<String, String> postParams = new HashMap<>();

    public Request(HttpMethod httpMethod, String path, HashMap<String, String> postParams) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.postParams = postParams;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, String> getPostParams() {
        return postParams;
    }
}
