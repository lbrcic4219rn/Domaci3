package glavni.app;

import glavni.http.Request;
import glavni.http.response.Response;

public abstract class Controller {

    protected Request request;

    public Controller(Request request) {
        this.request = request;
    }

    public abstract Response doGet();
    public abstract Response doPost();
}
