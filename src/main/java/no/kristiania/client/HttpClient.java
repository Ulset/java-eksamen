package no.kristiania.client;

import no.kristiania.HttpMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class HttpClient {

    int statusCode;
    private final HashMap<String, String> responseHeaders = new HashMap<>();
    private String body;


    //This reads the request coming from the client
    public HttpClient(String hostname, int port, String requestTarg) throws IOException {
        //GET REQ from the hostname with the req target
        String request = "GET " + requestTarg + " HTTP/1.1\r\n" +
                "Host: " + hostname + "\r\n" +
                "\r\n";
        Socket socket = new Socket(hostname, port);
        socket.getOutputStream().write(request.getBytes());

        String statusCodeLine = HttpMessage.readLine(socket).split(" ")[1];
        this.statusCode = parseInt(statusCodeLine);

        //Gets the headerlines and puts them in a hashmap
        String headerLine;
        while (!(headerLine = HttpMessage.readLine(socket)).isEmpty()) {
            //We split the request string so we can store the key and value from the request. "Content-Length":"40" <- split on the colon will give us both values
            int colonPos = headerLine.indexOf(":");
            String headerKey = headerLine.substring(0, colonPos);
            String headerValue = headerLine.substring(colonPos + 1).trim();

            //Headerkey can be "Content-Length", and the value is the actual length of the content
            responseHeaders.put(headerKey, headerValue);
        }

        //Gets the total amount of the body
        int contentLength = parseInt(getResponseHeader("Content-Length"));
        StringBuilder bodyBuilder = new StringBuilder();
        //We read each byte and cast them to a char, and then after this process is done we set the body of the request to our newly built string.
        for (int i = 0; i < contentLength; i++) {
            int c = socket.getInputStream().read();
            bodyBuilder.append((char) c);
        }
        this.body = bodyBuilder.toString();
    }


    //These are just getters for our private instance variables
    public int getStatusCode() {
        return this.statusCode;
    }

    public String getResponseHeader(String key) {
        return responseHeaders.get(key);
    }

    public String getResponseBody() {
        return this.body;
    }
}
