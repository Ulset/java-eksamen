package no.kristiania.db;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskDao extends BaseDao {
    public TaskDao() throws IOException {
        super();
    }

    public TaskDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insertTask(String taskName, String status) throws SQLException {
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO tasks (taskname, status) VALUES (?, ?)")) {
                statement.setString(1, taskName);
                statement.setString(2, status);
                statement.executeUpdate();
            }
        }
    }

    public ArrayList<Task> getAllTasks() throws SQLException {
        ArrayList<Task> output = new ArrayList<>();
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT taskId, taskname, status from tasks")) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        //As long as the ResultSet has information, we will append this to our index page.
                        Task newTask = new Task(rs.getInt("taskId"), rs.getString("taskname"), rs.getString("status"));
                        output.add(newTask);
                    }
                }
            }
        }
        return output;
    }

    public Task getTask(String taskInput) throws SQLException {
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT taskId, taskname, status from tasks WHERE taskname = ?")) {
                statement.setString(1, taskInput);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        //As long as the ResultSet has information, we will append this to our index page.
                        Task newTask = new Task(rs.getInt("taskId"), rs.getString("taskname"), rs.getString("status"));
                        return newTask;
                    }
                }
            }
        }
        return null;
    }

    public void assignTask(Task targetTask, Member targetMember) throws SQLException {
        String query = "INSERT INTO taskref (userId, taskId) VALUES (?, ?)";
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, targetMember.getMemberId());
                statement.setInt(2, targetTask.getTaskId());
                statement.executeUpdate();
            }
        }
    }

    public ArrayList<Member> getMembersAssignedToTask(Task task) throws SQLException, IOException {
        String query = "SELECT userId, fullname, email  FROM members\n" +
                "WHERE userid IN (\n" +
                "SELECT userid FROM taskref\n" +
                "WHERE taskid = ?\n)";
        ArrayList<Member> output = new ArrayList<>();

        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, task.getTaskId());
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String memberName = rs.getString("fullname");
                        int memberId = rs.getInt("userId");
                        String memberEmail = rs.getString("email");
                        Member newMember = new Member(memberId, memberName, memberEmail);
                        output.add(newMember);
                    }
                }
            }
        }
        return output;
    }

    public void changeStatus(Task taskEl, String newStatus) throws SQLException {
        String query = "UPDATE tasks SET status=? WHERE taskName=?";
        try (Connection connection = this.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, newStatus);
                statement.setString(2, taskEl.getTaskName());
                statement.executeUpdate();
            }
        }
    }
}
