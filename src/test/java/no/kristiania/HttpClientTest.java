package no.kristiania;

import no.kristiania.client.HttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {
    @Test
    void shouldReturn200StatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo");
        Assertions.assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldReturn404StatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?status=404");
        Assertions.assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldReturnContentLength() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?body=ThomasFyr");
        Assertions.assertEquals("9", client.getResponseHeader("Content-Length"));
    }

    @Test
    void shouldReturnContentBody() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?body=ThomasFyr");
        Assertions.assertEquals("ThomasFyr", client.getResponseBody());
    }
}