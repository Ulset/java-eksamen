package no.kristiania.server.controllers.Get;

import no.kristiania.HttpMessage;
import no.kristiania.QueryString;
import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import no.kristiania.db.Task;
import no.kristiania.db.TaskDao;
import no.kristiania.server.HttpResponse;
import no.kristiania.server.controllers.HttpController;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetTasksController implements HttpController {

    TaskDao taskDao;

    public GetTasksController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {

        StringBuilder out = new StringBuilder();
        out.append("<ul>");

        for (Task taskObj : taskDao.getAllTasks()) {
            ArrayList<Member> assignedMembers = taskDao.getMembersAssignedToTask(taskObj);
            StringBuilder txt = new StringBuilder();
            String txtStart = "<li>" +
                    "<b>Navn:</b> " + taskObj.getTaskName() + " <br>" +
                    "<b>Status:</b> " + taskObj.getTaskStatus() + "<br>";
            txt.append(txtStart);
            if (assignedMembers.size() > 0) {
                txt.append("<b>Tildelt:</b> ");
                for (Member memberEl : assignedMembers) {
                    txt.append(memberEl.getFullName() + ", ");
                }
                txt.replace(txt.length() - 2, txt.length(), "");
                txt.append("<br>");
            }
            txt.append("</li>");
            out.append(txt);
        }
        out.append("</ul>");
        HttpResponse response = new HttpResponse(out.toString());
        response.sendTo(clientSocket);
    }
}
