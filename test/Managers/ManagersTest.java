package Managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    void setUp() {
        inMemoryHistoryManager = new InMemoryHistoryManager(10);
    }

    @Test
    void testOfGettingDefault() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        assertEquals(inMemoryTaskManager.getClass(), Managers.getDefault().getClass());

    }

    @Test
    void testOfGettingHistory() {
        assertEquals(inMemoryHistoryManager.getClass(), Managers.getDefaultHistory().getClass());
    }
}