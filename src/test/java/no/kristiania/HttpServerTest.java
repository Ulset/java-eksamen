package no.kristiania;

import no.kristiania.client.HttpClient;
import no.kristiania.server.HttpServer;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    private JdbcDataSource dataSource;
    private HttpServer server;


    @BeforeEach
    void setUp() throws IOException {
        dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        server = new HttpServer(0, dataSource);
    }

    @Test
    void shouldReturnSuccessfulStatusCode() throws IOException {
        System.out.printf("Server");

        System.out.printf("klient");
        HttpClient client = new HttpClient("localhost", server.getPort(), "/index.html");
        System.out.println("Equals");
        Assertions.assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldReturnUnsuccessfullyStatusCode() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/invalid.html");
        Assertions.assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldReturnContentLength() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/testBody.html");
        Assertions.assertEquals("15", client.getResponseHeader("Content-Length"));
    }

    @Test
    void shouldReturnResponsebody() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/testBody.html");
        Assertions.assertEquals("<p>IT WORKS</p>", client.getResponseBody());
    }
}
