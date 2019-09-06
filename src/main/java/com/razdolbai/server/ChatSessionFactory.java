package com.razdolbai.server;

import java.io.*;
import java.net.Socket;

public class ChatSessionFactory implements SessionFactory {
    private CommandFactory commandFactory;
    private SessionStore sessionStore;

    public ChatSessionFactory(CommandFactory commandFactory, SessionStore sessionStore) {
        this.commandFactory = commandFactory;
        this.sessionStore = sessionStore;
    }

    @Override
    public Session createSession(Socket socket) throws IOException {
        final BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(
                                socket.getInputStream())));
        final PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                socket.getOutputStream())));
        return new ChatSession(null, in, out, commandFactory, sessionStore, null);
    }
}
