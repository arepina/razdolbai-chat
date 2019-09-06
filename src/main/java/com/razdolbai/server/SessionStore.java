package com.razdolbai.server;

public interface SessionStore {
    void sendToAll(String message);
    void sendTo(String message, String chattersName);
    void register(Session session);
    void remove(Session session);
    void closeAll();
}
