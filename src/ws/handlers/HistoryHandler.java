package ws.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager tm;
    private final Gson gson;

    public HistoryHandler(Gson gson, TaskManager tm) {
        super();
        this.gson = gson;
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");

        if (exchange.getRequestMethod().equals("GET")) {
            if (path.length == 2) {
                sendText(exchange, gson.toJson(tm.getHistory()));
            } else {
                sendNotFound(exchange, "Endpoint " + path[3] + " not found");
            }
        } else {
            sendNotFound(exchange, "Endpoint " + exchange.getRequestMethod() + " not found");
        }
    }


}



