package managers;

import tasks.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager;


    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();

        Task task1 = new Task("title", "description", TaskStatus.NEW);
        Task task2 = new Task("title", "description", TaskStatus.NEW);
        Task task3 = new Task("title", "description", TaskStatus.NEW);

        Epic epic1 = new Epic("title", "description");
        Epic epic2 = new Epic("title", "description");

        taskManager.addTask(task1); // id 1
        taskManager.addTask(task2); // id 2
        taskManager.addTask(task3); // id 3

        taskManager.addEpic(epic1); // id 4
        taskManager.addEpic(epic2); // id 5

        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, 4);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, 4);

        taskManager.addSubTask(subTask1); // id 6
        taskManager.addSubTask(subTask2); // id 7
    }



    @Test
    void getTasks() {
        Task task1 = new Task("title", "description", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("title", "description", TaskStatus.NEW);
        task2.setId(2);
        Task task3 = new Task("title", "description", TaskStatus.NEW);
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
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, 4);
        subTask1.setId(6);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, 4);
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
        Task task1 = new Task("title", "description", TaskStatus.NEW);
        task1.setId(1);
        assertEquals(task1, taskManager.getTaskById(1));
    }

    @Test
    void getSubTaskById() {
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, 4);
        subTask1.setId(6);
        assertEquals(subTask1, taskManager.getSubTaskById(6));
    }

    @Test
    void getEpicById() {
        Epic epic1 = new Epic("title", "description");
        epic1.setId(4);
        assertEquals(epic1, taskManager.getEpicById(4));
    }


    @Test
    void updateTask() {
        Task task1 = new Task("new title", "new  description", TaskStatus.DONE);
        task1.setId(1);

        taskManager.updateTask(task1);
        assertEquals(task1, taskManager.getTaskById(1));
    }

    @Test
    void updateEpic() {
        Epic epic1 = new Epic("new title", "new  description");
        epic1.setId(4);
        taskManager.updateEpic(epic1);
        assertEquals(epic1, taskManager.getEpicById(4));
    }

    @Test
    void updateSubTask() {
        SubTask subTask1 = new SubTask("new title", " new description", TaskStatus.DONE, 4);
        subTask1.setId(6);
        taskManager.updateSubTask(subTask1);
        assertEquals(subTask1, taskManager.getSubTaskById(6));

    }

    @Test
    void removeTaskById() {
        Task task1 = new Task("title", "description", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("title", "description", TaskStatus.NEW);
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
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, 4);
        subTask1.setId(6);

        ArrayList<SubTask> list = new ArrayList<>();
        list.add(subTask1);

        taskManager.removeSubTaskById(7);

        assertEquals(list, taskManager.getSubTasks());
    }

    @Test
    void getSubtaskByEpic() {
        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, 4);
        subTask1.setId(6);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, 4);
        subTask2.setId(7);

        ArrayList<SubTask> list = new ArrayList<>();
        list.add(subTask1);
        list.add(subTask2);

        assertEquals(list, taskManager.getSubtaskByEpicId(4));

    }
    @Test @AfterAll
    static void getHistory() {
        // AfterAll потому что экземпляр менеджеров один, а порядок тестов непонятен

        // сброс менеджеров в исходной состояние
        Managers.taskManager.removeAllEpics();
        Managers.taskManager.removeAllTasks();
        Managers.taskManager.removeAllSubTasks();
        Managers.historyManager.clearHistory();


        ArrayList<Task> list = new ArrayList<>();

        Task task1 = new Task("title", "description", TaskStatus.NEW);
        Managers.taskManager.addTask(task1);
        list.add(Managers.taskManager.getTaskById(1));

        Task task2 = new Task("title", "description", TaskStatus.NEW);
        Managers.taskManager.addTask(task2);
        list.add(Managers.taskManager.getTaskById(2));

        Managers.taskManager.removeTaskById(1);
        list.remove(0);


        List<Task> history = Managers.getDefault().getHistory();

        assertEquals(list, history);
    }
}