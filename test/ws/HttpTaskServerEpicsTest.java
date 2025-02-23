package ws;

import exceptions.NotFoundException;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerEpicsTest {
    TaskManager tm = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(tm);
    HttpClient client = HttpClient.newHttpClient();
    String context = "epics";


    public HttpTaskServerEpicsTest() throws IOException {
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
    void GetEpics() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String fromTM = taskServer.getGson().toJson(tm.getEpics());

        assertEquals(fromTM, response.body());

    }

    @Test
    void GetEpicById() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context + "/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task taskFromTM = null;
        if (tm.getEpicById(1).isPresent()) {
            taskFromTM = tm.getEpicById(1).get();
        }

        String fromTM = taskServer.getGson().toJson(taskFromTM);

        assertEquals(fromTM, response.body());

    }

    @Test
    void GetEpicId404() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context + "/22");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }


    @Test
    void PutEpic() throws IOException, InterruptedException {


        Epic epic1 = new Epic("title", "description");


        epic1.setId(null);
        epic1.setStartTime(LocalDateTime.now());
        epic1.setDuration(5);
        epic1.setEndTime(LocalDateTime.now().plusMinutes(5));

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskServer.getGson().toJson(epic1))).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        epic1.setId(5);

        Task taskFromTM = null;
        if (tm.getEpicById(5).isPresent()) {
            taskFromTM = tm.getEpicById(5).get();
        }

        assertEquals(epic1, taskFromTM);

    }


    @Test
    void GetEpicSubtasks() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context + "/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<SubTask> taskFromTM = tm.getSubtaskByEpicId(1);

        String fromTM = taskServer.getGson().toJson(taskFromTM);

        assertEquals(fromTM, response.body());

    }


    @Test
    void DeleteEpicById() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context + "/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        try {
            tm.getEpicById(1);
        } catch (NotFoundException e) {
            String expectedMessage = "Epic not found";
            assertEquals(expectedMessage, e.getMessage());
        }

    }


    @AfterEach
    void stopServer() {
        taskServer.stop();
    }
}
