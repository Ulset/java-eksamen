package no.kristiania.server;

import no.kristiania.HttpMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    /*
    The HttpResponse class will autogenerate a response that can be sent back to the user
    The hashmap 'headers' is if response needs extra headers, all the default headers (i.e Content-Length) is
    autogenerated
     */
    Map<String, String> headers;
    String body;
    int statusCode = 200;
    private boolean file = false;
    private ByteArrayOutputStream fileBuffer;

    public HttpResponse(String body) {
        defaultHeaders();
        this.body = body;
    }

    public HttpResponse(InputStream resourceStream, String filename) throws IOException {
        defaultHeaders();
        //Error handling.
        if (resourceStream == null) {
            body = "Cant find file on path " + filename;
            setStatusCode(404);
            return;
        }
        this.file = true;
        //If no error, we then respond with the requested html page
        fileBuffer = new ByteArrayOutputStream();
        resourceStream.transferTo(fileBuffer);
        //This allows us to send css files aswell
        if (filename.endsWith(".css")) {
            headers.put("Content-type", "text/css");
        }
        //This allows us to send js files aswell
        else if (filename.endsWith(".js")) {
            headers.put("Content-type", "text/css");
        } else if (filename.endsWith(".html")) {
            headers.put("Content-type", "text/html");
        }
    }


    private void defaultHeaders() {
        headers = new HashMap<>();
        headers.put("Content-type", "text/plain");
        headers.put("Connection", "close");
    }

    public void sendTo(Socket client) throws IOException {
        String statusCodeText = statusCode == 200 ? "OK" : "ERROR";
        StringBuilder out = new StringBuilder();
        out.append("HTTP/1.1 " + Integer.toString(statusCode) + " " + statusCodeText + "\r\n");
        for (Map.Entry<String, String> entryEl : headers.entrySet()) {
            out.append(entryEl.getKey() + ": " + entryEl.getValue() + "\r\n");
        }
        if (file) {
            out.append("Content-Length: " + fileBuffer.toByteArray().length + "\r\n");
            out.append("\r\n");
            client.getOutputStream().write(out.toString().getBytes());
            client.getOutputStream().write(fileBuffer.toByteArray());
        } else {
            out.append("Content-Length: " + body.length() + "\r\n");
            out.append("\r\n");
            out.append(body);
            client.getOutputStream().write(out.toString().getBytes());
        }
    }

    public void setHeader(String headerName, String headerValue) {
        this.headers.put(headerName, headerValue);
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
