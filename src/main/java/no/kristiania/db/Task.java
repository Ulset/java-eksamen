package no.kristiania.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Task {
    private final int taskId;
    private final String taskName;
    private final String status;

    public Task(int taskId, String taskName, String status) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskStatus() {
        return this.status;
    }

    public int getTaskId() {
        return this.taskId;
    }
}
