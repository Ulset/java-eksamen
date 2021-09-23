package no.kristiania.server.controllers.Post;

import no.kristiania.HttpMessage;
import no.kristiania.QueryString;
import no.kristiania.db.TaskDao;
import no.kristiania.server.HttpResponse;
import no.kristiania.server.controllers.HttpController;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLException;

public class AddTaskController implements HttpController {
    TaskDao taskDao;

    public AddTaskController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        QueryString input = new QueryString("/justfill?" + message.getBody());
        String inputTask = input.getHeader("task_name");
        String inputTaskStatus = input.getHeader("task_status");
        String resp;
        if (inputTask == null || inputTaskStatus == null) {
            resp = "Du må skrive inn noen verdier!";
        } else {
            String taskDecoded = URLDecoder.decode(inputTask, "UTF-8");
            String statusDecoded = URLDecoder.decode(inputTaskStatus, "UTF-8");

            this.taskDao.insertTask(taskDecoded, statusDecoded);

            resp = "Oppgave '" + taskDecoded + "' lagt til.";
        }

        HttpResponse response = new HttpResponse(resp);
        response.sendTo(clientSocket);
    }
}
