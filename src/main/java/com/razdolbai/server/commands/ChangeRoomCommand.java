package com.razdolbai.server.commands;

import com.razdolbai.server.Identificator;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.OccupiedNicknameException;
import com.razdolbai.server.history.saver.Saver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ChangeRoomCommand implements Command {
    private final Session session;
    private final Identificator identificator;
    private final LocalDateTime timestamp;
    private final SessionStore sessionStore;
    private final Saver saver;
    private final String newRoom;

    public ChangeRoomCommand(Session session, Identificator identificator, LocalDateTime timestamp, SessionStore sessionStore, Saver saver, String newRoom) {
        this.session = session;
        this.identificator = identificator;
        this.timestamp = timestamp;
        this.sessionStore = sessionStore;
        this.saver = saver;
        this.newRoom = newRoom;
    }

    @Override
    public void execute() throws OccupiedNicknameException, IOException {
        String nick = session.getUsername();
        String oldRoom = session.getRoom();
        sendChangedRoomMessage(newRoom, oldRoom, nick);
        session.setRoom(newRoom);
    }

    private void sendChangedRoomMessage(String newRoom, String oldRoom, String nick) throws IOException, OccupiedNicknameException {
        String message = "";
        if (oldRoom == null) {
            identificator.changeRoom(nick, newRoom);
            message = nick + " is now in " + newRoom;
            String decoratedMessage = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + message;
            identificator.getNicknames().entrySet().stream()
                    .filter(x -> Objects.equals(newRoom, x.getValue()))
                    .forEach(x -> sessionStore.sendTo(decoratedMessage, x.getKey()));
            saver.save(decoratedMessage, timestamp);
        } else {
            message = nick + " from " + oldRoom + " has changed room to " + newRoom;
            String decoratedMessage = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + message;
            identificator.getNicknames().entrySet().stream()
                    .filter(x -> Objects.equals(oldRoom, x.getValue()))
                    .forEach(x -> sessionStore.sendTo(decoratedMessage, x.getKey()));
            saver.save(decoratedMessage, timestamp);
            identificator.changeRoom(nick, newRoom);
        }
    }
}
