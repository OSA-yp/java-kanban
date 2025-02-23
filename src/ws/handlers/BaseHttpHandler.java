package ws.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {


    public void sendText(HttpExchange exchange, String text) throws IOException {
        send(exchange, text, 200);
    }

    public void sendAddOK(HttpExchange exchange, String text) throws IOException {
        send(exchange, text, 201);
    }

    public void sendNotFound(HttpExchange exchange, String text) throws IOException {
        send(exchange, text, 404);
    }

    public void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        send(exchange, text, 406);
    }

    private void send(HttpExchange exchange, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}
