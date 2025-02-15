package managers;

import tasks.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    void getHistory() {  // этот тест в своей логике должен выполняться 1 раз, поэтому вынесен из TaskManagerTest

        // сброс менеджеров в исходной состояние

        TaskManager taskManager = Managers.getInMemoryManager();

        taskManager.removeAllEpics();
        taskManager.removeAllTasks();
        taskManager.removeAllSubTasks();
        Managers.historyManager.clearHistory();


        ArrayList<Task> list = new ArrayList<>();

        Task task1 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5);
        taskManager.addTask(task1);
        Optional<Task> optionalTask = taskManager.getTaskById(1);
        optionalTask.ifPresent(list::add);


        Task task2 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), 15);
        taskManager.addTask(task2);
        Optional<Task> optionalTask2 = taskManager.getTaskById(2);
        optionalTask2.ifPresent(list::add);


        taskManager.removeTaskById(1);
        list.remove(0);


        List<Task> history = Managers.getDefault().getHistory();

        assertEquals(list, history);
    }

}