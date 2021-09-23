package no.kristiania;

import no.kristiania.client.HttpClient;
import no.kristiania.server.HomemadeCookie;
import no.kristiania.server.HttpServer;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class HomemadeCookieTest {

    @Test
    void shouldGetCookieName(){
        HomemadeCookie cookie = new HomemadeCookie("Ulsetorianos Servers");
        String name = "Ulsetorianos Servers";

        assertEquals(name, cookie.getCookieName());
    }

    @Test
    void shouldGetCookieValue(){
        HomemadeCookie cookie = new HomemadeCookie("Ulsetorianos Servers");
        String value = cookie.getCookieValue();
        assertEquals(value, cookie.getCookieValue());
    }

    @Test
    void shouldRecieveCookie() throws IOException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        HttpServer serv = new HttpServer(6969, dataSource);
        HttpClient client = new HttpClient("localhost", 6969, "/");
        assertNotSame(client.getResponseHeader("Set-Cookie"), null);
    }
}
