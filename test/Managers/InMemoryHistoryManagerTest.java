package Managers;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void testAddingTasksAndGetHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager(10);

        ArrayList<Task> list = new ArrayList<>();

        Task task1 = new Task("title", "description", TaskStatus.NEW);
        Task task2 = new Task("title", "description", TaskStatus.NEW);
        Task task3 = new Task("title", "description", TaskStatus.NEW);
        Task task4 = new Task("title", "description", TaskStatus.NEW);
        Task task5 = new Task("title", "description", TaskStatus.NEW);
        Task task6 = new Task("title", "description", TaskStatus.NEW);
        Epic epic1 = new Epic("title", "description");
        Epic epic2 = new Epic("title", "description");
        SubTask subTask1 = new SubTask("title", "description",TaskStatus.NEW, 999);
        SubTask subTask2 = new SubTask("title", "description",TaskStatus.NEW, 999);
        SubTask subTask3 = new SubTask("title", "description",TaskStatus.NEW, 999);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.add(task6);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(subTask1);
        historyManager.add(subTask2); // 10
        historyManager.add(subTask3); // 11

        list.add(task1);
        list.add(task2);
        list.add(task3);
        list.add(task4);
        list.add(task5);
        list.add(task6);
        list.add(epic1);
        list.add(epic2);
        list.add(subTask1);
        list.add(subTask2); // 10
        list.add(subTask3); // 11

        // сдвиг при достижении границы
        list.remove(0);


        assertEquals(list, historyManager.getHistory());
    }
}