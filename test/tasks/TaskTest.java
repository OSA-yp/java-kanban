package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    Task task1;

    // TODO новые поля

    @BeforeEach
    void setUp(){
        task1 = new Task("title", "description", TaskStatus.NEW, 60, LocalDateTime.now());
    }

    @Test
    void testSetterAndGetterForId() {
        task1.setId(1);
        assertEquals(1, task1.getId());
    }

    @Test
    void testSetterAndGetterForTitle() {
        task1.setTitle("new title");
        assertEquals("new title", task1.getTitle());
    }

    @Test
    void testSetterAndGetterForDescription() {
        task1.setDescription("new description");
        assertEquals("new description", task1.getDescription());
    }

    @Test
    void testSetterAndGetterForStatus() {
        task1.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task1.getStatus());
    }

    @Test
    void testToString() {
        task1.setId(1);
        assertEquals("Task.Task{id=1, title='title', description='description', status=NEW}", task1.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Task task1 = new Task("title", "description", TaskStatus.NEW);
        Task task2 = new Task("title", "description", TaskStatus.NEW);
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());

        task2.setId(999);
        assertNotEquals(task1, task2);
    }


}
