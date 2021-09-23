package no.kristiania.server.controllers.Post;

import no.kristiania.HttpMessage;
import no.kristiania.QueryString;
import no.kristiania.db.Task;
import no.kristiania.db.TaskDao;
import no.kristiania.server.controllers.HttpController;

import java.io.IOException;
import java.net.Socket;

import no.kristiania.server.HttpResponse;

import java.net.URLDecoder;
import java.sql.SQLException;

public class ChangeStatusController implements HttpController {
    TaskDao taskDao;

    public ChangeStatusController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        QueryString input = new QueryString("/justfill?" + message.getBody());
        String taskname = URLDecoder.decode(input.getHeader("task_name"), "UTF-8");
        String newStatus = URLDecoder.decode(input.getHeader("new_status"), "UTF-8");
        System.out.println(newStatus);
        Task taskEl = taskDao.getTask(taskname);
        if (taskEl == null) {
            String resp = "Finner ikke task med navn: " + taskname;
            new HttpResponse(resp).sendTo(clientSocket);
            return;
        }
        taskDao.changeStatus(taskEl, newStatus);
        String resp = "Endret status på task '" + taskname + " til " + newStatus;
        HttpResponse response = new HttpResponse(resp);
        response.sendTo(clientSocket);
    }
}
