package no.kristiania.server.controllers;

import no.kristiania.HttpMessage;
import no.kristiania.QueryString;
import no.kristiania.db.MemberDao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;

public class AddMemberController implements HttpController {

    MemberDao memberDao;
    public AddMemberController(MemberDao memberDao){
        this.memberDao = memberDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        QueryString input = new QueryString("/justfill?"+message.getBody());
        //memberDao is our database access object and by using the query string, we put the value of fullname and email address into the database.
        String inputName = input.getHeader("full_name");
        String inputEmail = input.getHeader("email_address");
        String resp;
        if(inputName == null || inputEmail == null){
            resp = "Du må skrive inn noen verdier!!";
        }
        else {
            //URL Encoder instead of our own class
            String nameDecoded = URLDecoder.decode(inputName, "UTF-8");
            String emailDecoded = URLDecoder.decode(inputEmail, "UTF-8");


            //Insert the decoded string into our database, this allows the request to go through, and nice formatting
            this.memberDao.insertUser(nameDecoded, emailDecoded);
            resp = "Bruker '"+ nameDecoded +"' lagt til.";
        }

        //To keep it simple, we just write a simple response string to the html site, so the user has to navigate back - but the content they added will be there!
        String response = "HTTP/1.1 200 OK\r\n"+
                "Content-Type: text/plain\r\n"+
                "Content-Length: "+resp.length()+"\r\n"+
                "\r\n"+
                resp;
        // Write the response back to the client
        clientSocket.getOutputStream().write(response.getBytes());
    }
}
