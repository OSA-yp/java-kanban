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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerSubTasksTest {
    TaskManager tm = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(tm);
    HttpClient client = HttpClient.newHttpClient();
    String context = "subtasks";


    public HttpTaskServerSubTasksTest() throws IOException {
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

        tm.addEpic(epic1); // id 1
        tm.addSubTask(subTask1); // id 2
        tm.addSubTask(subTask2); // id 3


    }

    @Test
    void GetSubTasks() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String fromTM = taskServer.getGson().toJson(tm.getSubTasks());

        assertEquals(fromTM, response.body());

    }

    @Test
    void GetSubTaskById() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context + "/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task taskFromTM = null;
        if (tm.getSubTaskById(2).isPresent()) {
            taskFromTM = tm.getSubTaskById(2).get();
        }

        String fromTM = taskServer.getGson().toJson(taskFromTM);

        assertEquals(fromTM, response.body());

    }

    @Test
    void GetSubTaskById404() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context + "/22");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }


    @Test
    void PutSubTask() throws IOException, InterruptedException {


        SubTask subTask = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(55), 5, 1);

        String jsonTask = taskServer.getGson().toJson(subTask);

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        subTask.setId(4);
        Task taskFromTM = null;
        if (tm.getSubTaskById(4).isPresent()) {
            taskFromTM = tm.getSubTaskById(4).get();
        }

        assertEquals(subTask, taskFromTM);

    }

    @Test
    void PutSubTask406() throws IOException, InterruptedException {


        SubTask subTask = new SubTask("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(5), 5, 1);

        String jsonTask = taskServer.getGson().toJson(subTask);

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());


    }

    @Test
    void PostSubTaskUpdate() throws IOException, InterruptedException {


        SubTask subTask = new SubTask("UPDATE", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(55), 5, 1);
        subTask.setId(2);
        String jsonTask = taskServer.getGson().toJson(subTask);

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());


        Task taskFromTM = null;
        if (tm.getSubTaskById(2).isPresent()) {
            taskFromTM = tm.getSubTaskById(2).get();
        }

        assertEquals(subTask, taskFromTM);

    }


    @Test
    void PostSubTaskUpdate404() throws IOException, InterruptedException {


        SubTask subTask = new SubTask("UPDATE", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(55), 5, 1);
        subTask.setId(11);
        String jsonTask = taskServer.getGson().toJson(subTask);

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }

    @Test
    void PostSubTaskUpdate406() throws IOException, InterruptedException {


        SubTask subTask = new SubTask("UPDATE", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(5), 15, 1);
        subTask.setId(2);
        String jsonTask = taskServer.getGson().toJson(subTask);

        URI url = URI.create("http://localhost:8080/" + context);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());

    }

    @Test
    void DeleteSubTaskById() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/" + context + "/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        try {
            tm.getTaskById(1);
        } catch (NotFoundException e) {
            String expectedMessage = "Task not found";
            assertEquals(expectedMessage, e.getMessage());
        }

    }


    @AfterEach
    void stopServer() {
        taskServer.stop();
    }
}
