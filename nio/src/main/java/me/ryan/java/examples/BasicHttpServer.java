package me.ryan.java.examples;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class BasicHttpServer {
    public static final int HTTP_PORT = 8090;

    private static final Logger log = LogManager.getLogger(BasicHttpServer.class);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(BasicHttpServer::handleRequest);
        server.start();
        log.info("Http Basic server started on {}...", HTTP_PORT);
        log.info("Press RETURN to stop...");
        System.in.read();
        server.stop(0);
    }

    private static void handleRequest(HttpExchange httpExchange) throws IOException {
        String response = "From Java Basic Http Server";
        httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
}
