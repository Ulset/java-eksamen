package no.kristiania.server.controllers.Post;

import no.kristiania.HttpMessage;
import no.kristiania.QueryString;
import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import no.kristiania.db.Task;
import no.kristiania.server.HttpResponse;
import no.kristiania.server.controllers.HttpController;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;

public class ChangeMemberNameController implements HttpController {
    MemberDao memberDao;

    public ChangeMemberNameController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        QueryString input = new QueryString("/justfill?" + message.getBody());

        String currentName = URLDecoder.decode(input.getHeader("current_name"), "UTF-8");
        String newName = URLDecoder.decode(input.getHeader("new_name"), "UTF-8");

        Member currMember = memberDao.getMember(currentName);

        if (currMember == null) {
            String resp = "Finner ikke task med navn: " + currentName;
            new HttpResponse(resp).sendTo(clientSocket);
            return;
        }
        memberDao.changeName(currMember, newName);
        String resp = "Endret navn fra '" + currMember.getFullName() + " til " + newName;
        HttpResponse response = new HttpResponse(resp);
        response.sendTo(clientSocket);
    }
}
