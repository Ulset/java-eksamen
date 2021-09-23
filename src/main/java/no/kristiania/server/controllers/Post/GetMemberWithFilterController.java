package no.kristiania.server.controllers.Post;

import no.kristiania.HttpMessage;
import no.kristiania.QueryString;
import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import no.kristiania.server.HttpResponse;
import no.kristiania.server.controllers.HttpController;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;

public class GetMemberWithFilterController implements HttpController {
    MemberDao memberDao;

    public GetMemberWithFilterController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        //New stringbuilder to prepare a response
        QueryString input = new QueryString("/justfill?" + message.getBody());
        String filter = URLDecoder.decode(input.getHeader("filter"), "UTF-8");
        StringBuilder out = new StringBuilder();
        out.append("<ul>");
        //We loop through our members in the database
        for (Member memberObj : memberDao.getAllMembers()) {
            if (memberObj.getFullName().startsWith(filter)) {
                //and create a list element with the full name and email
                String txt = "<li>" +
                        "<b>Navn:</b> " + memberObj.getFullName() + " <br>" +

                        "<b>Epost:</b> " + memberObj.getEmail() +
                        "</li>";
                out.append(txt);
            }
        }
        out.append("</ul>");
        HttpResponse response = new HttpResponse(out.toString());
        response.sendTo(clientSocket);
    }
}
