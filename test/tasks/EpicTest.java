package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epic1;

    @BeforeEach
    void setUp() {
        epic1 = new Epic("title", "description");
    }


    @Test
    void testAddingSubTaskAndGettingTasksIdsList() {
        epic1.addSubTask(1);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);

        assertEquals(list, epic1.getSubTasksIds());
    }

    @Test
    void removeSubTask() {
        epic1.addSubTask(1);
        epic1.removeSubTask(1);

        ArrayList<Integer> list = new ArrayList<>();
        assertEquals(list, epic1.getSubTasksIds());

    }

    @Test
    void removeAllSubTasks() {
        epic1.addSubTask(1);
        epic1.removeAllSubTasks();

        ArrayList<Integer> list = new ArrayList<>();
        assertEquals(list, epic1.getSubTasksIds());
    }

    @Test
    void testToString() {
        epic1.setId(100);
        epic1.addSubTask(1);
        epic1.setStartTime(LocalDateTime.parse("13.02.2025 20:46", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        assertEquals("Task.Epic{id=100, title='title', description='description', status=NEW', startTime=13.02.2025 20:46, duration=0, subTasks IDs=[1]}", epic1.toString());
    }
}