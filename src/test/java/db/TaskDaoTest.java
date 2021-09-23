package db;

import no.kristiania.RandomThings;
import no.kristiania.db.Member;
import no.kristiania.db.MemberDao;
import no.kristiania.db.Task;
import no.kristiania.db.TaskDao;
import org.checkerframework.checker.units.qual.A;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskDaoTest {

    @BeforeEach
    void setUp(){
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
    }

    private DataSource getTestDatabase() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    private Task getTestTask() throws SQLException {
        TaskDao db = new TaskDao(getTestDatabase());
        RandomThings randomThings = new RandomThings();
        String name = randomThings.getRandomName(10);
        String status = randomThings.getRandomStatus();
        db.insertTask(name, status);
        return db.getTask(name);
    }

    @Test
    void shouldRetrieveTaskname() throws SQLException, IOException {
        Task testTask = getTestTask();
        DataSource testDb = getTestDatabase();
        TaskDao taskDao = new TaskDao(testDb);
        taskDao.insertTask(testTask.getTaskName(), testTask.getTaskStatus());
        ArrayList<Task> allTasks = taskDao.getAllTasks();
        ArrayList<String> allTaskNames = new ArrayList<>();
        for(Task taskEl : allTasks){
            allTaskNames.add(taskEl.getTaskName());
        }
        assertThat(allTaskNames).contains(testTask.getTaskName());
    }

    @Test
    void shouldAssignUserToTask() throws SQLException, IOException {
        Task testTask = getTestTask();
        Member testMember = MemberDaoTest.getTestMember();
        TaskDao db = new TaskDao(getTestDatabase());
        db.assignTask(testTask, testMember);
        ArrayList<Member> assignedMembers = db.getMembersAssignedToTask(testTask);
        ArrayList<String> memberNames = new ArrayList<>();
        for(Member memberEl: assignedMembers){
            memberNames.add(memberEl.getFullName());
        }
        assertThat(memberNames).contains(testMember.getFullName());
    }
}
