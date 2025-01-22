package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    void testOfGettingDefault() throws IOException {
        File file = File.createTempFile("tasks", null);
        TaskManager taskManager = new FileBackedTaskManager(file);

        assertEquals(taskManager.getClass(), Managers.getDefault().getClass());

    }

    @Test
    void testOfGettingHistory() {
        assertEquals(inMemoryHistoryManager.getClass(), Managers.getDefaultHistory().getClass());
    }
}