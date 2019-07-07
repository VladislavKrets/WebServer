package com.server;

import com.server.executors.Connection;
import com.server.logger.Logger;
import com.server.mappers.PageMapperBuilder;
import com.server.mappers.PageMapperContainer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class Server {
    private int port;
    private Logger logger;

    public Server(){
        this(8080);
    }

    public Server(int port) {
        this.port = port;
        logger = new Logger();
    }

    public void execute() {
        PageMapperContainer pageMapperContainer = PageMapperContainer.getInstance();
        pageMapperContainer.addPage(new PageMapperBuilder()
                .setPath("404")
                .setHtml("<html><head><title>Not found</title></head>" +
                        "<body>404 Not Found</body></html>")
                .setGetRequest(params -> {
                    return new HashMap<>();
                })
                .getPageMapper());
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.printConsoleLog("server was started on port " + port);
            while (true) {
                new Connection(serverSocket.accept()).start();
                logger.printConsoleLog("connection accepted");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
