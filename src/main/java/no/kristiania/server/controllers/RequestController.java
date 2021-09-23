package no.kristiania.server.controllers;

import no.kristiania.HttpMessage;
import no.kristiania.db.MemberDao;
import no.kristiania.db.TaskDao;
import no.kristiania.server.HomemadeCookie;
import no.kristiania.server.HttpResponse;
import no.kristiania.server.controllers.Get.GetMemberController;
import no.kristiania.server.controllers.Get.GetTasksController;
import no.kristiania.server.controllers.Post.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RequestController implements HttpController {
    Map<String, HttpController> postControllers = new HashMap<>();
    Map<String, HttpController> getControllers = new HashMap<>();
    private final MemberDao memberDao;
    private final TaskDao taskDao;

    private void setControllers() {
        this.postControllers = Map.of(
                "/api/addMember", new AddMemberController(memberDao),
                "/api/addTask", new AddTaskController(taskDao),
                "/api/assignTask", new AssignTaskController(taskDao, memberDao),
                "/api/changeTaskStatus", new ChangeStatusController(taskDao),
                "/api/filterMember", new GetMemberWithFilterController(memberDao),
                "/api/filterTask", new FilterTaskController(taskDao),
                "/api/changeMemberName", new ChangeMemberNameController(memberDao)
        );

        this.getControllers = Map.of(
                "/api/getMembers", new GetMemberController(memberDao),
                "/api/getTasks", new GetTasksController(taskDao)
        );
    }

    public RequestController() throws IOException {
        this.memberDao = new MemberDao();
        this.taskDao = new TaskDao();
        setControllers();
    }

    public RequestController(DataSource dataSource) {
        this.memberDao = new MemberDao(dataSource);
        this.taskDao = new TaskDao(dataSource);
        setControllers();
    }

    @Override
    public void handle(Socket clientSocket, HttpMessage message) throws SQLException, IOException {
        //This gets the request type from our httpmessage and allows us to do checks, so we can handle request properly.
        String reqType = message.getReqType();
        System.out.println(reqType + " " + message.getReqTarget());
        //Two separate methods to handle two different requesttypes.
        if (reqType.equals("GET")) {
            handleGet(clientSocket, message);
        } else if (reqType.equals("POST")) {
            handlePost(clientSocket, message);
        }
    }

    //This is where we handle the post request, and in our case this allows us to add members to our database.
    private void handlePost(Socket clientSocket, HttpMessage message) throws IOException, SQLException {
        HttpController controller = postControllers.get(message.getReqTarget());
        if (controller != null) {
            controller.handle(clientSocket, message);
        }
    }

    //This handles our get requests
    private void handleGet(Socket clientSocket, HttpMessage message) throws IOException, SQLException {
        HttpController controller = getControllers.get(message.getReqTarget());
        if (controller != null) {
            controller.handle(clientSocket, message);
            return;
        }

        //If there is no controller(API) it means that the client is asking for a local file.

        if (message.getReqTarget().equals("/")) {
            message.setReqTarget("/index.html");
        }
        //Our get request asks for a resource, and we will respond accordingly
        String filename = message.getReqTarget();
        InputStream resourceStream = getClass().getResourceAsStream(filename);


        HttpResponse response = new HttpResponse(resourceStream, filename);
        if (message.getHeader("Cookie") == null) {
            //If user does not have a cookie, set one.
            HomemadeCookie coolCookie = new HomemadeCookie("Ulsetianos server");
            response.setHeader("Set-Cookie", coolCookie.getCookieValue());
        }
        response.sendTo(clientSocket);
    }
}
