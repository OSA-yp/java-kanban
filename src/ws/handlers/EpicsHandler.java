package ws.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager tm;
    private final Gson gson;


    public EpicsHandler(Gson gson, TaskManager tm) {
        super();
        this.gson = gson;
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");

        switch (exchange.getRequestMethod()) {
            case "GET":
                switch (path.length) {
                    case 2:
                        String text = gson.toJson(tm.getEpics());
                        sendText(exchange, text);
                        break;
                    case 3:
                        int id = Integer.parseInt(path[2]);

                        Epic epic = null;
                        try {
                            Optional<Epic> mayBeEpic = tm.getEpicById(id);
                            epic = mayBeEpic.get();
                        } catch (NotFoundException e) {
                            sendNotFound(exchange, "Epic with id=" + id + " not found");
                        }
                        sendText(exchange, gson.toJson(epic));

                        break;
                    case 4:
                        if (Objects.equals(path[3], "subtasks")) {
                            int epicId = Integer.parseInt(path[2]);
                            Optional<Epic> optionalEpic = tm.getEpicById(epicId);
                            if (optionalEpic.isPresent()) {
                                sendText(exchange, gson.toJson(tm.getSubtaskByEpicId(epicId)));

                            } else {
                                sendNotFound(exchange, "Epic with id=" + epicId + " not found");
                            }


                        } else {
                            sendNotFound(exchange, "Endpoint " + path[3] + " not found");
                        }
                }
                break;
            case "POST":
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic taskFromPost = gson.fromJson(body, Epic.class);
                if (taskFromPost.getId() == null) {
                    int res = tm.addEpic(taskFromPost);
                    switch (res) {
                        case -1:
                            sendNotFound(exchange, "Nothing to add");
                            break;
                        case -2:
                            sendNotFound(exchange, "Epic has wrong subtasks");
                            break;

                        default:
                            sendAddOK(exchange, gson.toJson(res));
                            break;
                    }
                } else {
                    int res = tm.updateEpic(taskFromPost);
                    switch (res) {
                        case 1:
                            sendAddOK(exchange, "Epic with id=" + taskFromPost.getId() + " updated");
                            break;
                        case -1:
                            sendNotFound(exchange, "Epic with id=" + taskFromPost.getId() + " not found");
                            break;
                        case -2:
                            sendNotFound(exchange, "Epic has wrong subtasks");
                            break;

                    }
                }
                break;

            case "DELETE":
                int id = Integer.parseInt(path[2]);
                int res = tm.removeEpicById(id);
                if (res == 1) {
                    sendText(exchange, "Epic with id=" + id + " removed");
                } else if (res == -1) {
                    sendNotFound(exchange, "Epic with id=" + id + " not found");
                }
            default:
                sendNotFound(exchange, "Endpoint " + exchange.getRequestMethod() + " not found");
        }
    }
}

