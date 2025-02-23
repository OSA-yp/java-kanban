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
    private TaskManager tm;


    private final Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {

        server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimetTypeAdapter());
        gsonBuilder.serializeNulls();
        this.gson = gsonBuilder.create();
        this.tm = taskManager;

        server.createContext("/tasks", new TasksHandler(gson, tm));
        server.createContext("/subtasks", new SubTasksHandler(gson, tm));
        server.createContext("/epics", new EpicsHandler(gson, tm));
        server.createContext("/history", new HistoryHandler(gson, tm));
        server.createContext("/prioritized", new PrioritizedHandler(gson, tm));


    }


    public Gson getGson() {
        return gson;
    }


    public void start() {
        server.start();
        System.out.println("TaskServer is started");
    }

    public void stop() {
        server.stop(0);
        System.out.println("TaskServer is stoped");
    }


    public static void main(String[] args) throws IOException {
        // TODO
        TaskManager tm = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(tm);
        taskServer.start();



    }
}
