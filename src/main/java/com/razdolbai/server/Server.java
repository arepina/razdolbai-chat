package com.razdolbai.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;

public class Server {
    private ServerSocket connectionListener;
    private SessionFactory sessionFactory;
    private SessionStore sessionStore;
    private static final Logger log = Logger.getLogger(Server.class);

    public Server(SessionFactory sessionFactory, SessionStore sessionStore) {
        this.sessionFactory = sessionFactory;
        this.sessionStore = sessionStore;
    }

    void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(8082)) {
            connectionListener = serverSocket;
            registerShutdownHook();
            while (true) {
                Socket socket = connectionListener.accept();
                Session session = sessionFactory.createSession(socket);
                sessionStore.register(session);
            }
        } catch (IOException e) {
            log.error("Exception: " + e);
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (connectionListener != null) {
                try {
                    sessionStore.sendToAll("Server died ;<");
                    sessionStore.closeAll();
                } catch (Exception e) {
                    log.error("Exception: " + e);
                }
            }
            System.out.println("Server closed");
        }));
    }
}
