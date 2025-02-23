package ws;

import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpTaskServerPrioritizedTest {

    TaskManager tm = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(tm);
    HttpClient client = HttpClient.newHttpClient();
    String context = "prioritized";

    public HttpTaskServerPrioritizedTest() throws IOException {
    }


    @BeforeEach
    void setUp() {
        taskServer.start();
        tm.removeAllEpics();
        tm.removeAllSubTasks();
        tm.removeAllTasks();

        SubTask subTask1 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(5), 5, 1);
        SubTask subTask2 = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(15), 5, 1);
        Epic epic1 = new Epic("title", "description");
        Epic epic2 = new Epic("title", "description");

        tm.addEpic(epic1); // id 1
        tm.addSubTask(subTask1); // id 2
        tm.addSubTask(subTask2); // id 3
        tm.addEpic(epic2); // id 4

    }

    @Test
    void GetPrioritized() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String fromTM = taskServer.getGson().toJson(tm.getPrioritizedTasks());

        assertEquals(fromTM, response.body());

    }

    @AfterEach
    void stopServer() {
        taskServer.stop();
    }
}
