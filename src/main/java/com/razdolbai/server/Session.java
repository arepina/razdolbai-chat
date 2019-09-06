package com.razdolbai.server;

public interface Session extends Runnable{
    String getUsername();
    void setUsername(String nickname);
    void setRoom(String room);
    String getRoom();
    void run();
    void send(String message);
    void close();
}
