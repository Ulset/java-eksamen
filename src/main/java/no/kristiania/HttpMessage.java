package no.kristiania;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class HttpMessage {

    private final String type;
    private final QueryString reqLine;
    private HashMap<String, String> httpHeaders = new HashMap<>();
    private String body;


    public HttpMessage(Socket client) throws IOException {
        String firstLine = readLine(client);
        type = firstLine.split(" ")[0];
        reqLine = new QueryString(firstLine.split(" ")[1]);
        readHeaders(client);
        if (!(httpHeaders.get("Content-Length") == null)) {
            readBody(client);
        }
    }

    private void readBody(Socket client) throws IOException {
        int contentLength = Integer.parseInt(httpHeaders.get("Content-Length"));
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            bodyBuilder.append((char) client.getInputStream().read());
        }
        this.body = bodyBuilder.toString();
    }

    private void readHeaders(Socket client) throws IOException {
        //This while loop will run aslong as the http request from the client is not empty/finished
        String newLine;
        while (!(newLine = readLine(client)).isEmpty()) {
            //Then we split the values in to using regex, and put these values into our httpHeaders hashmap.
            String[] newLineSplit = newLine.split(":");
            String httpHead = newLineSplit[0].trim();
            String httpValue = newLineSplit[1].trim();
            httpHeaders.put(httpHead, httpValue);
        }
    }


    public static String readLine(Socket socket) throws IOException {
        StringBuilder line = new StringBuilder();
        int c;
        while ((c = socket.getInputStream().read()) != -1) {
            if (c == '\r') {
                socket.getInputStream().read();
                break;
            }
            line.append((char) c);
        }
        return line.toString();
    }

    public void setReqTarget(String target) {
        this.reqLine.setTarget(target);
    }

    public String getReqType() {
        return type;
    }

    public String getReqTarget() {
        return reqLine.getTarget();
    }

    public String getHeader(String header) {
        return httpHeaders.get(header);
    }

    public String getBody() {
        return body;
    }
}
