package main.http;

import main.app.RequestHandler;
import main.http.response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread implements Runnable {

    private final Socket client;
    private static final Logger LOGGER = Logger.getLogger(ServerThread.class.getName());

    public ServerThread(Socket sock) {
        this.client = sock;
    }

    public void run() {
        try (var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             var out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true)) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isBlank()) return;

            StringTokenizer tokenizer = new StringTokenizer(requestLine);
            String method = tokenizer.nextToken();
            String path = tokenizer.nextToken();

            int contentLength = 0;
            String line;
            while ((line = in.readLine()) != null && !line.trim().isEmpty()) {
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.split(": ")[1].trim());
                }
            }

            Map<String, String> postParams = new HashMap<>();
            if (HttpMethod.POST.toString().equals(method) && contentLength > 0) {
                char[] buf = new char[contentLength];
                int bytesRead = in.read(buf);
                parsePostParams(new String(buf, 0, bytesRead), postParams);
            }

            Request request = new Request(HttpMethod.valueOf(method), path, postParams);
            Response response = new RequestHandler().handle(request);
            out.print(response.getResponseString());
            out.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "I/O error while handling client request", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while handling client request", e);
        }
    }

    private void parsePostParams(String body, Map<String, String> params) {
        for (String param : body.split("&")) {
            String[] parts = param.split("=", 2);
            if (parts.length == 2) {
                params.put(
                        URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                );
            }
        }
    }
}
