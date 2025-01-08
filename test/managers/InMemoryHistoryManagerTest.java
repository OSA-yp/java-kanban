package managers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    @BeforeEach
    void setUp() {


        Task task1 = new Task("title", "description", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("title", "description", TaskStatus.NEW);
        task2.setId(2);
        Task task3 = new Task("title", "description", TaskStatus.NEW);
        task3.setId(3);
        Task task4 = new Task("title", "description", TaskStatus.NEW);
        task4.setId(4);
        Task task5 = new Task("title", "description", TaskStatus.NEW);
        task5.setId(5);
        Task task6 = new Task("title", "description", TaskStatus.NEW);
        task6.setId(6);
        Epic epic1 = new Epic("title", "description");
        epic1.setId(7);
        Epic epic2 = new Epic("title", "description");
        epic2.setId(8);
        SubTask subTask1 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask1.setId(9);
        SubTask subTask2 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask2.setId(10);
        SubTask subTask3 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask3.setId(11);

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
    }
    @Test
    void testAddingTasksAndGetHistory() {

        Task task1 = new Task("title", "description", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("title", "description", TaskStatus.NEW);
        task2.setId(2);
        Task task3 = new Task("title", "description", TaskStatus.NEW);
        task3.setId(3);
        Task task4 = new Task("title", "description", TaskStatus.NEW);
        task4.setId(4);
        Task task5 = new Task("title", "description", TaskStatus.NEW);
        task5.setId(5);
        Task task6 = new Task("title", "description", TaskStatus.NEW);
        task6.setId(6);
        Epic epic1 = new Epic("title", "description");
        epic1.setId(7);
        Epic epic2 = new Epic("title", "description");
        epic2.setId(8);
        SubTask subTask1 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask1.setId(9);
        SubTask subTask2 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask2.setId(10);
        SubTask subTask3 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask3.setId(11);

        ArrayList<Task> list = new ArrayList<>();
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

        assertEquals(list, historyManager.getHistory());
    }

    @Test
    void testRemovingTasksFromHistory(){
        Task task1 = new Task("title", "description", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("title", "description", TaskStatus.NEW);
        task2.setId(2);
        Task task3 = new Task("title", "description", TaskStatus.NEW);
        task3.setId(3);
        Task task4 = new Task("title", "description", TaskStatus.NEW);
        task4.setId(4);
        Task task5 = new Task("title", "description", TaskStatus.NEW);
        task5.setId(5);
        Task task6 = new Task("title", "description", TaskStatus.NEW);
        task6.setId(6);
        Epic epic1 = new Epic("title", "description");
        epic1.setId(7);
        Epic epic2 = new Epic("title", "description");
        epic2.setId(8);
        SubTask subTask1 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask1.setId(9);
        SubTask subTask2 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask2.setId(10);
        SubTask subTask3 = new SubTask("title", "description",TaskStatus.NEW, 999);
        subTask3.setId(11);

        ArrayList<Task> list = new ArrayList<>();
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

        // удаляем первый, осталось 10
        list.remove(0);
        historyManager.remove(1);

        // удаляем из середины, осталось 9
        list.remove(5);
        historyManager.remove(7);

        // удаляем последний
        list.remove(8);
        historyManager.remove(11);


        assertEquals(list, historyManager.getHistory());
    }
}
