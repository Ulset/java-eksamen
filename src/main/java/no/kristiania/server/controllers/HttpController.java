package no.kristiania.server.controllers;

import no.kristiania.HttpMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.SQLException;

public interface HttpController {
    void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException;
}
