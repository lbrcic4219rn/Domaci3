package glavni.http;

import glavni.app.RequestHandler;
import glavni.http.response.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Socket sock) {
        this.client = sock;

        try {
            //inicijalizacija ulaznog toka
            in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()));

            //inicijalizacija izlaznog sistema
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    client.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // uzimamo samo prvu liniju zahteva, iz koje dobijamo HTTP method i putanju
            String requestLine = in.readLine();

            StringTokenizer stringTokenizer = new StringTokenizer(requestLine);

            String method = stringTokenizer.nextToken();
            String path = stringTokenizer.nextToken();
            int contentLen = 0;
            System.out.println("\nHTTP ZAHTEV KLIJENTA:\n");
            do {
                System.out.println(requestLine);
                requestLine = in.readLine();
                if(requestLine.contains("Content-Length")){
                    contentLen = Integer.parseInt(requestLine.split(": ")[1]);
                }
            } while (!requestLine.trim().equals(""));

            HashMap<String, String> postParams = new HashMap<>();

            if (method.equals(HttpMethod.POST.toString())) {
                char[] buf = new char[contentLen];
                in.read(buf);
                String params = new String(buf);
                String[] paramArr = params.split("&");
                for (String param: paramArr) {
                    System.out.println("PARAM: " + param);
                    postParams.put(param.split("=")[0],
                            URLDecoder.decode(param.split("=")[1], StandardCharsets.UTF_8.name()));
                }
            }

            Request request = new Request(HttpMethod.valueOf(method), path, postParams);

            RequestHandler requestHandler = new RequestHandler();
            Response response = requestHandler.handle(request);

            System.out.println("\nHTTP odgovor:\n");
            System.out.println(response.getResponseString());

            out.println(response.getResponseString());

            in.close();
            out.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
