package ws.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.SubTask;
import ws.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubTasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager tm = HttpTaskServer.getTaskManager();
    private final Gson gson;


    public SubTasksHandler(Gson gson) {
        super();
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");

        switch (exchange.getRequestMethod()) {
            case "GET":
                if (path.length == 2) {
                    String text = gson.toJson(tm.getSubTasks());

                    sendText(exchange, text);
                } else {
                    int id = Integer.parseInt(path[2]);
                    Optional<SubTask> mayBeSubTask = tm.getSubTaskById(id);
                    if (mayBeSubTask.isPresent()) {
                        sendText(exchange, gson.toJson(mayBeSubTask.get()));
                    } else {
                        sendNotFound(exchange, "Subtask with id=" + id + " not found");
                    }
                }
                break;
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                SubTask taskFromPost = gson.fromJson(body, SubTask.class);
                if (taskFromPost.getId() == null) {
                    int res = tm.addSubTask(taskFromPost);
                    switch (res) {
                        case -1:
                            sendNotFound(exchange, "Nothing to add");
                            break;
                        case -2:
                            sendHasInteractions(exchange, "New subtask has interactions, restricted to add");
                            break;
                        case -3:
                            sendNotFound(exchange, "Subtask with id=" + taskFromPost.getId() + " has wrong epic id=" + taskFromPost.getParentEpicId());
                            break;
                        default:
                            sendAddOK(exchange, gson.toJson(res));
                            break;
                    }
                } else {
                    int res = tm.updateSubTask(taskFromPost);
                    switch (res) {
                        case 1:
                            sendAddOK(exchange, "Subtask with id=" + taskFromPost.getId() + " updated");
                            break;
                        case -1:
                            sendNotFound(exchange, "Subtask with id=" + taskFromPost.getId() + " not found");
                            break;
                        case -2:
                            sendHasInteractions(exchange, "Subtask has interactions, restricted to update");
                            break;
                        case -3:
                            sendNotFound(exchange, "Subtask with id=" + taskFromPost.getId() + " has wrong epic id=" + taskFromPost.getParentEpicId());
                            break;
                    }
                }
                break;

            case "DELETE":
                int id = Integer.parseInt(path[2]);
                int res = tm.removeSubTaskById(id);
                if (res == 1) {
                    sendText(exchange, "Subtask with id=" + id + " removed");
                } else if (res == -1) {
                    sendNotFound(exchange, "Subtask with id=" + id + " not found");
                }
            default:
                sendNotFound(exchange, "Endpoint " + exchange.getRequestMethod() + " not found");
        }
    }
}

