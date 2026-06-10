package main.http.response;

public class HtmlResponse extends Response {
    private final String html;

    public HtmlResponse(String html) {
        this.html = html;
    }

    @Override
    public int getStatusCode() {
        return 200;
    }

    @Override
    public String getContentType() {
        return "text/html; charset=UTF-8";
    }

    @Override
    public String getBody() {
        return html;
    }
}
