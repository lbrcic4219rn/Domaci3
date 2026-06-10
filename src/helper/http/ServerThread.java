package helper.http;

import com.google.gson.Gson;
import main.http.HttpMethod;
import helper.app.QuoteStorage;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread implements Runnable {
    private final Socket socket;
    private final Gson gson = new Gson();
    private static final Logger LOGGER = Logger.getLogger(ServerThread.class.getName());

    public ServerThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (var s = this.socket;
            var in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            var out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true)){

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isBlank()) return;

            StringTokenizer tokenizer = new StringTokenizer(requestLine);
            String method = tokenizer.nextToken();
            String path = tokenizer.nextToken();

            if (HttpMethod.GET.toString().equals(method)
                    && "/".equals(path)) {
                sendJson(out);
            } else {
                send404(out);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while handling client request", e);
        }
    }

    private void sendJson(PrintWriter out) {
        int index = ThreadLocalRandom.current().nextInt(QuoteStorage.getInstance().getQuoteList().size());
        String json = gson.toJson(QuoteStorage.getInstance().getQuoteList().get(index));

        out.print("HTTP/1.1 " + 200 + " OK\r\n");
        out.print("Content-Type: application/json\r\n");
        out.print("Content-Length: " + json.getBytes().length + "\r\n");
        out.print("\r\n");
        out.print(json);
        out.flush();
    }

    private void send404(PrintWriter out) {
        out.print("HTTP/1.1 404 Not Found\r\n");
        out.print("Content-Length: 0\r\n");
        out.print("\r\n");
        out.flush();
    }
}
