package ws;

import exceptions.NotFoundException;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTasksTest {
    TaskManager tm = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(tm);
    HttpClient client = HttpClient.newHttpClient();


    public HttpTaskServerTasksTest() throws IOException {
    }


    @BeforeEach
    void setUp() {
        taskServer.start();
        tm.removeAllEpics();
        tm.removeAllSubTasks();
        tm.removeAllTasks();

        Task task1 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now(), 5);
        Task task2 = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), 15);

        tm.addTask(task1);
        tm.addTask(task2);

    }

    @Test
    void GetTasks() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        String fromTM = taskServer.getGson().toJson(tm.getTasks());

        assertEquals(fromTM, response.body());

    }

    @Test
    void GetTaskById() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task taskFromTM = null;
        if (tm.getTaskById(1).isPresent()) {
            taskFromTM = tm.getTaskById(1).get();
        }

        String fromTM = taskServer.getGson().toJson(taskFromTM);

        assertEquals(fromTM, response.body());

    }

    @Test
    void GetTaskById404() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/tasks/17");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }


    @Test
    void PutTask() throws IOException, InterruptedException {


        Task task = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(60), 15);

        String jsonTask = taskServer.getGson().toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        task.setId(3);
        Task taskFromTM = null;
        if (tm.getTaskById(3).isPresent()) {
            taskFromTM = tm.getTaskById(3).get();
        }

        assertEquals(task, taskFromTM);

    }

    @Test
    void PutTask406() throws IOException, InterruptedException {


        Task task = new Task("title", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), 15);

        String jsonTask = taskServer.getGson().toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());


    }

    @Test
    void PostTaskUpdate() throws IOException, InterruptedException {


        Task task = new Task("UPDATE", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(60), 15);
        task.setId(1);
        String jsonTask = taskServer.getGson().toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());


        Task taskFromTM = null;
        if (tm.getTaskById(1).isPresent()) {
            taskFromTM = tm.getTaskById(1).get();
        }

        assertEquals(task, taskFromTM);

    }


    @Test
    void PostTaskUpdate404() throws IOException, InterruptedException {


        Task task = new Task("UPDATE", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(60), 15);
        task.setId(11);
        String jsonTask = taskServer.getGson().toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }

    @Test
    void PostTaskUpdate406() throws IOException, InterruptedException {


        Task task = new Task("UPDATE", "description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(10), 15);
        task.setId(1);
        String jsonTask = taskServer.getGson().toJson(task);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(jsonTask)).build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());

    }

    @Test
    void DeleteTaskById() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/tasks/1");
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
