
package com.devops;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(
            new InetSocketAddress(8090), 0
        );

        server.createContext("/", exchange -> {

            String response = """
                <html>
                <head><title>DevOps Project</title></head>
                <body style="text-align:center; font-family:Arial;">
                    <h1>Welcome to DevOps CI/CD Project!</h1>
                    <h2>Application deployed successfully!</h2>
                    <p>Built using Java and Maven.</p>
                </body>
                </html>
                """;

            exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");

            exchange.sendResponseHeaders(200,
                response.getBytes().length);

            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.start();
        System.out.println("Server running on port 8090");
    }
}

