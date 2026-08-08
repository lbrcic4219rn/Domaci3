package http;

import http.response.InternalServerErrorResponse;
import http.response.PlainTextResponse;
import http.response.Response;

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

    private static final Logger LOGGER = Logger.getLogger(ServerThread.class.getName());

    private final Socket client;
    private final RequestHandler handler;

    public ServerThread(Socket client, RequestHandler handler) {
        this.client = client;
        this.handler = handler;
    }

    @Override
    public void run() {
        try (var socket = this.client;
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             var out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isBlank()) return;

            StringTokenizer tokenizer = new StringTokenizer(requestLine);
            if (tokenizer.countTokens() < 2) {
                write(out, new PlainTextResponse(400, "Malformed request line"));
                return;
            }

            String method = tokenizer.nextToken();
            String path = tokenizer.nextToken();
            int contentLength = readHeaders(in);

            Map<String, String> postParams = new HashMap<>();
            if (HttpMethod.POST.name().equals(method) && contentLength > 0) {
                readPostParams(in, contentLength, postParams);
            }

            HttpMethod httpMethod = parseMethod(method);
            if (httpMethod == null) {
                write(out, new PlainTextResponse(501, "Unsupported method: " + method));
                return;
            }

            write(out, handle(new Request(httpMethod, path, postParams), method, path));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "I/O error while handling client request", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error while handling client request", e);
        }
    }

    private Response handle(Request request, String method, String path) {
        try {
            return handler.handle(request);
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Error while handling " + method + " " + path, t);
            return new InternalServerErrorResponse("Internal server error while handling " + path);
        }
    }

    private static int readHeaders(BufferedReader in) throws IOException {
        int contentLength = 0;
        String line;
        while ((line = in.readLine()) != null && !line.trim().isEmpty()) {
            if (line.toLowerCase().startsWith("content-length:")) {
                String value = line.split(":", 2)[1].trim();
                try {
                    contentLength = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Ignoring malformed Content-Length: " + value);
                }
            }
        }
        return contentLength;
    }

    private static void readPostParams(BufferedReader in, int contentLength, Map<String, String> params)
            throws IOException {
        char[] buffer = new char[contentLength];
        int read = in.read(buffer);
        if (read > 0) {
            parsePostParams(new String(buffer, 0, read), params);
        }
    }

    private static void parsePostParams(String body, Map<String, String> params) {
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

    private static HttpMethod parseMethod(String method) {
        for (HttpMethod candidate : HttpMethod.values()) {
            if (candidate.name().equals(method)) return candidate;
        }
        return null;
    }

    private static void write(PrintWriter out, Response response) {
        out.print(response.getResponseString());
        out.flush();
    }
}
