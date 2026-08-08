package http;

import java.util.Map;

public record Request(HttpMethod httpMethod, String path, Map<String, String> postParams) {
}
