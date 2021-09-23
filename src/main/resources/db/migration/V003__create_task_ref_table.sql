CREATE TABLE taskref (
    userId int,
    taskId int,
    FOREIGN KEY (userId) REFERENCES members(userId) ON DELETE CASCADE,
    FOREIGN KEY (taskId) REFERENCES tasks(taskId) ON DELETE CASCADE
);