package no.kristiania.server.controllers.Post;

import no.kristiania.HttpMessage;
import no.kristiania.QueryString;
import no.kristiania.db.MemberDao;
import no.kristiania.server.HttpResponse;
import no.kristiania.server.controllers.HttpController;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;

public class AddMemberController implements HttpController {
    MemberDao memberDao;

    public AddMemberController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        QueryString input = new QueryString("/justFill?" + message.getBody());

        String inputName = input.getHeader("full_name");
        String inputEmail = input.getHeader("email_address");
        String resp;
        if (inputName == null || inputEmail == null) {
            resp = "Du må skrive inn noen verdier!!";
        } else {
            //URL Encoder instead of our own class
            String nameDecoded = URLDecoder.decode(inputName, "UTF-8");
            String emailDecoded = URLDecoder.decode(inputEmail, "UTF-8");


            //Insert the decoded string into our database, this allows the request to go through, and nice formatting
            this.memberDao.insertUser(nameDecoded, emailDecoded);
            resp = "Bruker '" + nameDecoded + "' lagt til.";
        }

        HttpResponse response = new HttpResponse(resp);
        response.sendTo(clientSocket);
    }
}
