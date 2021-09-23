package no.kristiania.server.controllers.Post;

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
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilterTaskController implements HttpController {

    private TaskDao taskDao;

    public FilterTaskController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        //New stringbuilder to prepare a response
        QueryString input = new QueryString("/justfill?" + message.getBody());
        String taskFilterUncoded = input.getHeader("task_filter");
        String memberFilterUncoded = input.getHeader("member_filter");
        if (taskFilterUncoded == null) {
            taskFilterUncoded = "";
        }
        if (memberFilterUncoded == null) {
            memberFilterUncoded = "";
        }
        String taskFilter = URLDecoder.decode(taskFilterUncoded, "UTF-8");
        String memberFilter = URLDecoder.decode(memberFilterUncoded, "UTF-8");

        StringBuilder out = new StringBuilder();
        out.append("<ul>");


        for (Task taskObj : taskDao.getAllTasks()) {
            boolean taskFilterApproved = false;
            boolean memberFilterApproved = false;

            if (taskObj.getTaskName().startsWith(taskFilter) || taskFilter.isEmpty()) {
                taskFilterApproved = true;
            }
            ArrayList<Member> assignedMembers = taskDao.getMembersAssignedToTask(taskObj);
            for (Member memberObj : assignedMembers) {
                if (memberObj.getFullName().startsWith(memberFilter)) {
                    memberFilterApproved = true;
                }
            }
            if (memberFilter.isEmpty()) {
                memberFilterApproved = true;
            }


            if (taskFilterApproved && memberFilterApproved) {
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
                out.append(txt);
            }
        }
        out.append("</ul>");
        HttpResponse response = new HttpResponse(out.toString());
        response.sendTo(clientSocket);
    }
}
