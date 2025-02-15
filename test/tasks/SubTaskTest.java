package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    SubTask subTask;

    @BeforeEach
    void setUp() {
        subTask = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.parse("13.02.2025 20:47", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), 5, 999);
    }


    @Test
    void getParentEpicId() {
        assertEquals(999, subTask.getParentEpicId());
    }

    @Test
    void testToString() {
        subTask.setId(5);

        assertEquals("Task.SubTask{id=5, title='title', description='description', status=NEW', parentEpicId=999, startTime=13.02.2025 20:47, duration=5}", subTask.toString());

    }
}