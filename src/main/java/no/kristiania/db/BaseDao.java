package no.kristiania.db;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseDao {
    private DataSource dataSource;

    //This is a base class which has the functionality we need for all types of database access object we might create.
    public BaseDao(DataSource dataSource) {
        Flyway.configure().dataSource(dataSource).load().migrate();
        this.dataSource = dataSource;
    }

    //We use the properties file to gain access to our database - and set the connection up.
    public BaseDao() throws IOException {
        String propPath = "pgr203.properties";
        try (InputStream input = new FileInputStream(propPath)) {
            Properties prop = new Properties();
            prop.load(input);
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl(prop.getProperty("dataSource.url"));
            dataSource.setUser(prop.getProperty("dataSource.username"));
            dataSource.setPassword(prop.getProperty("dataSource.password"));
            this.dataSource = dataSource;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Kan ikke finne en .propterties fil på path: " + propPath);
        }
        Flyway.configure().dataSource(dataSource).load().migrate();
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public static void main(String[] args) throws IOException {
        new BaseDao();
    }
}
