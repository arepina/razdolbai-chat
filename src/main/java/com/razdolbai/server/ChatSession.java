package com.razdolbai.server;

import com.razdolbai.server.commands.Command;
import com.razdolbai.server.exceptions.ChatException;
import com.razdolbai.server.exceptions.OccupiedNicknameException;
import com.razdolbai.server.exceptions.UnidentifiedRoomException;
import com.razdolbai.server.exceptions.UnidentifiedUserException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class ChatSession implements Session {
    private String username;
    private String room;
    private Socket socket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private CommandFactory commandFactory;
    private boolean isClosed = false;
    private static final Logger log = Logger.getLogger(ChatSession.class);

    ChatSession(String username, Socket socket, BufferedReader socketIn, PrintWriter socketOut, CommandFactory commandFactory, String room) {
        this.username = username;
        this.room = room;
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.commandFactory = commandFactory;
    }

    @Override
    public void run() {
        try (
                BufferedReader myIn = socketIn
        ) {
            while (!isClosed) {
                String message = myIn.readLine();
                processRequest(message);
            }
        } catch (IOException e) {
            log.error("Exception: " + e);
        }
    }

    @Override
    public void send(String message) {
        socketOut.println(message);
        socketOut.flush();
    }

    @Override
    public void close() {
        isClosed = true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String getRoom() {
        return room;
    }

    private void processRequest(String message) throws IOException {
        LocalDateTime timeStamp = LocalDateTime.now();
        Command command = commandFactory.createCommand(this, message, timeStamp);
        try {
            command.execute();
        } catch (UnidentifiedUserException e) {
            log.info("Exception: " + e);
            processException(e, "First command should be /chid");
        } catch (UnidentifiedRoomException e) {
            log.info("Exception: " + e);
            processException(e, "Second command should be /chroom");
        } catch (OccupiedNicknameException e) {
            log.info("Exception: " + e);
            processException(e, "This nickname is occupied, try another one");
        } catch (ChatException e) {
            log.info("Exception: " + e);
            processException(e, "Some error has occurred");
        }
    }

    private void processException(Exception e, String message) {
        e.printStackTrace();
        send(message);
    }
}
