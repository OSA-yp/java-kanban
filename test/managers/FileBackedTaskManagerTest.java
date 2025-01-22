package managers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tasks.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    @Test
    void emptyFileTest() throws IOException {
        File file = File.createTempFile("tasks", null);
        TaskManager taskManager = new FileBackedTaskManager(file);
        List<tasks.Task> list = new ArrayList<>();
        List<Task> listFromFile = taskManager.getTasks();

        assertEquals(list,  listFromFile);
    }

    @Test
    void saveAndLoadTasksTest() throws IOException {

        File file = File.createTempFile("tasks", null);
        TaskManager taskManager1 = new FileBackedTaskManager(file);

        Task task1 = new Task("Задача номер 1", "Это 1я  тестовая задача для теста", TaskStatus.NEW);
        Task task2 = new Task("Задача номер два", "Это вторая  тестовая задача для теста", TaskStatus.NEW);
        taskManager1.addTask(task1);
        taskManager1.addTask(task2);

        TaskManager taskManager2 = new FileBackedTaskManager(file);

        assertEquals(taskManager1.getTasks(), taskManager2.getTasks());

    }
}
