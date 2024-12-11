package Tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    SubTask subTask;

    @BeforeEach
    void setUp(){
        subTask = new SubTask("title", "description",TaskStatus.NEW, 999);
    }


    @Test
    void getParentEpicId() {
        assertEquals(999, subTask.getParentEpicId());
    }

    @Test
    void testToString() {
        subTask.setId(5);

        assertEquals("Task.SubTask{id=5, title='title', description='description', status=NEW', parentEpicId=999}", subTask.toString());

    }
}