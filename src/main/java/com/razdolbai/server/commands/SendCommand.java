package com.razdolbai.server.commands;

import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.UnidentifiedRoomException;
import com.razdolbai.server.exceptions.UnidentifiedUserException;
import com.razdolbai.server.history.saver.Saver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendCommand implements Command {
    private final Session session;
    private final SessionStore sessionStore;
    private final String message;
    private final Saver saver;
    private final LocalDateTime timestamp;

    public SendCommand(Session session, SessionStore sessionStore, String message, Saver saver, LocalDateTime timestamp) {
        this.session = session;
        this.sessionStore = sessionStore;
        this.message = message;
        this.saver = saver;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() throws UnidentifiedUserException, IOException, UnidentifiedRoomException {
        checkUsernameAndRoom();
        String decoratedMessage = decorate(message);
        sessionStore.sendToAll(decoratedMessage);
        saver.save(decoratedMessage, timestamp);
    }

    private String decorate(String message) {
        return "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " +
                session.getUsername() + ": " + session.getRoom() + ": " +
                message;
    }

    private void checkUsernameAndRoom() throws UnidentifiedUserException, UnidentifiedRoomException {
        String nickname = session.getUsername();
        String room = session.getRoom();
        if (nickname == null) {
            throw new UnidentifiedUserException();
        }
        if(room == null){
            throw new UnidentifiedRoomException();
        }
    }
}
