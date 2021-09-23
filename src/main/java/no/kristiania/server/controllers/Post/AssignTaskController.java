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

public class AssignTaskController implements HttpController {
    TaskDao taskDao;
    MemberDao memberDao;

    public AssignTaskController(TaskDao taskDao, MemberDao memberDao) {
        this.taskDao = taskDao;
        this.memberDao = memberDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        QueryString input = new QueryString("/justfill?" + message.getBody());
        String taskInput = URLDecoder.decode(input.getHeader("task_name"), "UTF-8");
        String userInput = URLDecoder.decode(input.getHeader("member_name"), "UTF-8");

        Task targetTask = taskDao.getTask(taskInput);
        Member targetMember = memberDao.getMember(userInput);
        String resp;
        if (targetTask == null || targetMember == null) {
            resp = "Beklager fant enten ikke bruker '" + userInput +
                    "' eller task '" + taskInput + "'";
        } else {
            taskDao.assignTask(targetTask, targetMember);
            resp = "Bruker " + targetMember.getFullName() + " assignet til task " + targetTask.getTaskName();
        }
        HttpResponse response = new HttpResponse(resp);
        response.sendTo(clientSocket);
    }
}
