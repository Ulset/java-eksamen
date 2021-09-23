package no.kristiania.server;

import no.kristiania.HttpMessage;
import no.kristiania.QueryString;
import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import no.kristiania.db.Task;
import no.kristiania.db.TaskDao;
import no.kristiania.server.controllers.*;

import javax.sql.DataSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    ServerSocket serverSocket;
    RequestController requestHandler;

    public static void main(String[] args) throws IOException {
        new HttpServer(8080);
    }

    //We have two constructors, incase a dataSource is not specified, then the server will use the default available
    public HttpServer(int port, DataSource dataSource) throws IOException {
        this.requestHandler = new RequestController(dataSource);
        startServerSocket(port);
    }

    public HttpServer(int port) throws IOException {
        this.requestHandler = new RequestController();
        startServerSocket(port);
    }

    private void startServerSocket(int port) throws IOException {
        //We create a new server socket for accepting clients on the port specified in our constructor
        serverSocket = new ServerSocket(port);
        //New thread, which allows for parallel processing of client requests
        new Thread(() -> {
            while (true) {
                try {
                    //Our client will be whichever socket tries to connect to our server
                    Socket client = serverSocket.accept();
                    //Handle the request
                    HttpMessage message = new HttpMessage(client);
                    this.requestHandler.handle(client, message);
                    //Close connection after request is handled. Cleanup
                    client.close();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //This allows us to get a local available port, so we dont have to hardcode it everytime we test or etc.
    public int getPort() {
        return serverSocket.getLocalPort();
    }
}

