package managers;

import java.io.File;

public class Managers {

    static final String TASKS_FILE_NAME = "tasksdb.csv";

    static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    static InMemoryTaskManager taskInMemoryManager = new InMemoryTaskManager();
    static FileBackedTaskManager taskFileBackedManager = new FileBackedTaskManager(new File(TASKS_FILE_NAME));

    public static TaskManager getDefault() {
        return taskFileBackedManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

    public static TaskManager getInMemoryManager() {
        return taskInMemoryManager;
    }

}
