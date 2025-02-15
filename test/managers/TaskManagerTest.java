package managers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {


    InMemoryTaskManager taskManager;


    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();

        Task task1 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5);
        Task task2 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(15), 5);
        Task task3 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(25), 5);

        Epic epic1 = new Epic("title", "description");
        Epic epic2 = new Epic("title", "description");

        taskManager.addTask(task1); // id 1
        taskManager.addTask(task2); // id 2
        taskManager.addTask(task3); // id 3

        taskManager.addEpic(epic1); // id 4
        taskManager.addEpic(epic2); // id 5

        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(35), 5, 4);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(45), 5, 4);

        taskManager.addSubTask(subTask1); // id 6
        taskManager.addSubTask(subTask2); // id 7
    }

    @Test
    void epicNewStatusTest() {
        //    a. Все подзадачи со статусом NEW.

        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(55), 5, epic1.getId());
        subTask1.setId(6);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(65), 5, epic1.getId());
        subTask2.setId(7);


        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        assertEquals(TaskStatus.NEW, epic1.getStatus());
    }

    @Test
    void epicDoneStatusTest() {
        //    b. Все подзадачи со статусом DONE.

        Epic epic1 = new Epic("title", "description");
        epic1.setId(412);
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("title", "description", TaskStatus.DONE, LocalDateTime.now().plusMinutes(75), 5, epic1.getId());
        subTask1.setId(6);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.DONE, LocalDateTime.now().plusMinutes(85), 5, epic1.getId());
        subTask2.setId(7);


        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        assertEquals(TaskStatus.DONE, epic1.getStatus());
    }

    @Test
    void epicNewAndDoneStatusTest() {
        //    a. Все подзадачи со статусом NEW.
//    d. Подзадачи со статусом IN_PROGRESS.

        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(95), 5, epic1.getId());
        subTask1.setId(6);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.DONE, LocalDateTime.now().plusMinutes(105), 5, epic1.getId());
        subTask2.setId(7);


        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    void epicInProgressStatusTest() {
//    d. Подзадачи со статусом IN_PROGRESS.

        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("title", "description", TaskStatus.IN_PROGRESS, LocalDateTime.now().plusMinutes(245), 5, epic1.getId());
        subTask1.setId(6);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.IN_PROGRESS, LocalDateTime.now().plusMinutes(255), 5, epic1.getId());
        subTask2.setId(7);


        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
    }


    @Test
    void getTasks() {
        Task task1 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5);
        task1.setId(1);
        Task task2 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), 5);
        task2.setId(2);
        Task task3 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(30), 5);
        task3.setId(3);

        ArrayList<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);
        list.add(task3);

        assertEquals(list, taskManager.getTasks());
    }

    @Test
    void getEpics() {
        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        Epic epic2 = new Epic("title", "description");
        epic2.setId(5);

        ArrayList<Epic> list = new ArrayList<>();
        list.add(epic1);
        list.add(epic2);

        assertEquals(list, taskManager.getEpics());
    }

    @Test
    void getSubTasks() {
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5, 4);
        subTask1.setId(6);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5, 4);
        subTask2.setId(7);

        ArrayList<SubTask> list = new ArrayList<>();
        list.add(subTask1);
        list.add(subTask2);

        assertEquals(list, taskManager.getSubTasks());
    }

    @Test
    void removeAllTypesOfTasks() {
        taskManager.removeAllTypesOfTasks();

        ArrayList<Task> list1 = new ArrayList<>();
        ArrayList<Epic> list2 = new ArrayList<>();
        ArrayList<SubTask> list3 = new ArrayList<>();

        assertEquals(list1, taskManager.getTasks());
        assertEquals(list2, taskManager.getEpics());
        assertEquals(list3, taskManager.getSubTasks());
    }

    @Test
    void removeAllTasks() {
        ArrayList<Task> list1 = new ArrayList<>();
        taskManager.removeAllTasks();
        assertEquals(list1, taskManager.getTasks());
    }

    @Test
    void removeAllEpics() {
        ArrayList<Epic> list2 = new ArrayList<>();
        taskManager.removeAllEpics();
        assertEquals(list2, taskManager.getEpics());
    }

    @Test
    void removeAllSubTasks() {
        ArrayList<SubTask> list3 = new ArrayList<>();
        taskManager.removeAllSubTasks();
        assertEquals(list3, taskManager.getSubTasks());
    }

    @Test
    void getTaskById() {
        Task task1 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5);
        task1.setId(1);
        assertEquals(task1, taskManager.getTaskById(1).get());
    }

    @Test
    void getSubTaskById() {
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5, 4);
        subTask1.setId(6);
        assertEquals(subTask1, taskManager.getSubTaskById(6).get());
    }

    @Test
    void getEpicById() {
        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        assertEquals(epic1, taskManager.getEpicById(4).get());
    }


    @Test
    void updateTask() {
        Task task1 = new Task("new title", "new  description", TaskStatus.DONE, LocalDateTime.now(), 5);
        task1.setId(1);

        taskManager.updateTask(task1);
        assertEquals(task1, taskManager.getTaskById(1).get());
    }

    @Test
    void updateEpic() {
        Epic epic1 = new Epic("new title", "new  description");
        epic1.setId(4);
        taskManager.updateEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(4).get());
    }

    @Test
    void updateSubTask() {
        SubTask subTask1 = new SubTask("new title", " new description", TaskStatus.DONE, LocalDateTime.now(), 5, 4);
        subTask1.setId(6);
        taskManager.updateSubTask(subTask1);
        assertEquals(subTask1, taskManager.getSubTaskById(6).get());

    }

    @Test
    void removeTaskById() {
        Task task1 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5);
        task1.setId(1);
        Task task2 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5);
        task2.setId(2);

        ArrayList<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);

        taskManager.removeTaskById(3);

        assertEquals(list, taskManager.getTasks());

    }

    @Test
    void removeEpicById() {
        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);

        ArrayList<Epic> list = new ArrayList<>();
        list.add(epic1);

        taskManager.removeEpicById(5);

        assertEquals(list, taskManager.getEpics());
    }

    @Test
    void removeSubTaskById() {
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5, 4);
        subTask1.setId(6);

        ArrayList<SubTask> list = new ArrayList<>();
        list.add(subTask1);

        taskManager.removeSubTaskById(7);

        assertEquals(list, taskManager.getSubTasks());
    }

    @Test
    void getSubtaskByEpic() {
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5, 4);
        subTask1.setId(6);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5, 4);
        subTask2.setId(7);

        ArrayList<SubTask> list = new ArrayList<>();
        list.add(subTask1);
        list.add(subTask2);

        assertEquals(list, taskManager.getSubtaskByEpicId(4));

    }

    @Test
    void getPrioritizedTasksTest() {
        Task task1 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.parse("13.02.2025 20:25", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), 15);

        Task task2 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.parse("13.02.2025 20:25", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), 5);

        taskManager.addTask(task1);
        int task2Id = taskManager.addTask(task2);

        assertEquals(-1, task2Id);
    }


}