package com.razdolbai.server;

import com.razdolbai.server.history.saver.Saver;
import com.razdolbai.server.history.saver.SwitchingFileSaver;

import java.io.IOException;

public class ServerLauncher {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("\0", ":");
        Saver saver = new SwitchingFileSaver();
        Identificator identificator = new Identificator();
        SessionStore sessionStore = new ChatSessionStore();
        CommandFactory commandFactory = new ChatCommandFactory(parser, sessionStore, saver, identificator);
        SessionFactory sessionFactory = new ChatSessionFactory(commandFactory);
        new Server(sessionFactory, sessionStore).startServer();
    }
}
