package ws.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager tm;
    private final Gson gson;

    public TasksHandler(Gson gson, TaskManager tm) {
        super();
        this.gson = gson;
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");

        switch (exchange.getRequestMethod()) {
            case "GET":
                if (path.length == 2) {
                    String text = gson.toJson(tm.getTasks());

                    sendText(exchange, text);
                } else {
                    int id = Integer.parseInt(path[2]);
                    Task task = null;
                    try {
                        Optional<Task> mayBeTask = tm.getTaskById(id);
                        task = mayBeTask.get();
                    } catch (NotFoundException e) {
                        sendNotFound(exchange, "Task with id=" + id + " not found");
                    }
                    sendText(exchange, gson.toJson(task));
                }
                break;
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task taskFromPost = gson.fromJson(body, Task.class);
                if (taskFromPost.getId() == null) {
                    int res = tm.addTask(taskFromPost);
                    if (res > 0) {
                        sendAddOK(exchange, gson.toJson(res));
                    } else if (res == -2) {
                        sendHasInteractions(exchange, "New task has interactions, restricted to add");
                    }

                } else {
                    int res = tm.updateTask(taskFromPost);
                    switch (res) {
                        case 1:
                            sendAddOK(exchange, "Task with id=" + taskFromPost.getId() + " updated");
                            break;
                        case -1:
                            sendNotFound(exchange, "Task with id=" + taskFromPost.getId() + " not found");
                            break;
                        case -2:
                            sendHasInteractions(exchange, "Task has interactions, restricted to update");
                            break;
                    }
                }
                break;

            case "DELETE":
                int id = Integer.parseInt(path[2]);
                int res = tm.removeTaskById(id);
                if (res == 1) {
                    sendText(exchange, "Task with id=" + id + " removed");
                } else if (res == -1) {
                    sendNotFound(exchange, "Task with id=" + id + " not found");
                }
            default:
                sendNotFound(exchange, "Endpoint " + exchange.getRequestMethod() + " not found");
        }
    }


}



