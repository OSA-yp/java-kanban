package ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import ws.adaptors.DurationTypeAdapter;
import ws.adaptors.LocalDateTimetTypeAdapter;
import ws.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    HttpServer server;
    private final static TaskManager taskManager = Managers.getDefault();
    private final Gson gson;

    public HttpTaskServer(Integer port) throws IOException {

        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimetTypeAdapter());
        gsonBuilder.serializeNulls();
        this.gson = gsonBuilder.create();


        server.createContext("/tasks", new TasksHandler(gson));
        server.createContext("/subtasks", new SubTasksHandler(gson));
        server.createContext("/epics", new EpicsHandler(gson));
        server.createContext("/history", new HistoryHandler(gson));
        server.createContext("/prioritized", new PrioritizedHandler(gson));


    }


    public static TaskManager getTaskManager() {
        return taskManager;
    }


    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }


    public static void main(String[] args) throws IOException {
        // TODO
        HttpTaskServer taskServer = new HttpTaskServer(8080);
        taskServer.start();
        System.out.println("TaskServer is started");


    }
}
