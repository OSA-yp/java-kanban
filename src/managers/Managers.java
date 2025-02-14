package managers;

import java.io.File;
import java.io.IOException;

public class Managers {

    static final String TASKS_FILE_NAME = "tasksdb.csv";

    static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    static InMemoryTaskManager taskInMemoryManager = new InMemoryTaskManager();
    static FileBackedTaskManager taskFileBackedManager;

    static {
        try {
            taskFileBackedManager = new FileBackedTaskManager(new File(TASKS_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
