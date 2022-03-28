package glavni.app;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

public class QuoteOfTheDayGetter {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson = new Gson();

    public Quote getQuoteOfTheDay() {
        try {
            socket = new Socket("localhost", 8114);
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );
            out = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()
                    ), true
            );

            StringBuilder quoteReq = new StringBuilder();
            quoteReq.append("GET / HTTP/1.1\r\nHost: localhost:8114\r\n\r\n");
            out.println(quoteReq.toString());

            String reqLine = in.readLine();
            do {
                System.out.println(reqLine);
                reqLine = in.readLine();
            } while (!reqLine.trim().equals(""));

            String quoteJson = in.readLine();
            System.out.println(quoteJson);

            Quote quote = gson.fromJson(quoteJson, Quote.class);

            socket.close();
            in.close();
            out.close();

            return quote;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
