package managers;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import tasks.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Test
    void emptyFileTest() throws IOException {
        File file = File.createTempFile("tasks", null);
        TaskManager taskManager = new FileBackedTaskManager(file);
        List<tasks.Task> list = new ArrayList<>();
        List<Task> listFromFile = taskManager.getTasks();

        assertEquals(list, listFromFile);
    }

    @Test
    void saveAndLoadTasksTest() throws IOException {

        File file = File.createTempFile("tasks", null);
        TaskManager taskManager1 = new FileBackedTaskManager(file);

        Task task1 = new Task("Задача номер 1", "Это 1я  тестовая задача для теста", TaskStatus.NEW, LocalDateTime.now(), 5);
        Task task2 = new Task("Задача номер два", "Это вторая  тестовая задача для теста", TaskStatus.NEW, LocalDateTime.now(), 5);
        taskManager1.addTask(task1);
        taskManager1.addTask(task2);

        TaskManager taskManager2 = new FileBackedTaskManager(file);

        assertEquals(taskManager1.getTasks(), taskManager2.getTasks());

    }

    @Test
    public void testException() throws IOException {

        File file = File.createTempFile("tasks", null);

        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false)) {

            fileWriter.write("id,type,name,status,description,epic"+ "\n");
            fileWriter.write("2,TASK,Задача номер два,INVALID,Это вторая  тестовая задача для теста,2025-02-14T18:06:55.641439400,15,"+ "\n");
        }

        try {
            // Тестируем, что исключение будет выброшено
            assertThrows(ManagerLoadException.class, () -> {
                TaskManager taskManager = new FileBackedTaskManager(file); // Здесь должно быть выброшено исключение
            }, "Загрузка из файла " + file.getName() + " не удалась");
        } finally {
            file.delete(); // Убедитесь, что временный файл будет удалён
        }
    }

}


