package com.razdolbai.server;

import com.razdolbai.common.CommandType;
import com.razdolbai.server.commands.*;
import com.razdolbai.server.history.HistoryAccessObject;
import com.razdolbai.server.history.saver.Saver;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatCommandFactory implements CommandFactory {
    private final Parser parser;
    private final SessionStore sessionStore;
    private final Saver saver;
    private final Identificator identificator;
    private final HistoryAccessObject history;
    private static final Logger log = Logger.getLogger(ChatCommandFactory.class);

    public ChatCommandFactory(Parser parser,
                              SessionStore sessionStore,
                              Saver saver,
                              Identificator identificator, HistoryAccessObject history) {
        this.parser = parser;
        this.sessionStore = sessionStore;
        this.saver = saver;
        this.identificator = identificator;
        this.history = history;
    }

    @Override
    public Command createCommand(Session session, String message, LocalDateTime timestamp) {
        Map<String, String> fieldMap = parser.parse(message);
        String type = fieldMap.get("type");
        CommandType commandType = CommandType.fromString(type);
        log.info(commandType + " " + message);
        switch (commandType) {
            case HIST:
                return createHistCommand(session);
            case SEND:
                return createSendCommand(session, fieldMap, timestamp);
            case CHID:
                return createChangeIdCommand(session, fieldMap, timestamp);
            case CLOSE:
                return createCloseCommand(session);
            case CHROOM:
                return createChroomCommand(session, fieldMap, timestamp);
            case SNDP:
                return createSendPCommand(session, fieldMap, timestamp);
            default:
                throw new IllegalArgumentException("Unknown command type: " + type);
        }
    }

    private Command createChroomCommand(Session session, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String newRoom = fieldMap.get("msg");
        return new ChangeRoomCommand(session, identificator, timestamp, sessionStore, saver, newRoom);
    }

    private Command createHistCommand(Session session) {
        return new HistoryCommand(session, history);
    }

    private Command createCloseCommand(Session session) {
        return new CloseCommand(session, sessionStore);
    }

    private SendCommand createSendCommand(Session session, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String message = fieldMap.get("msg");
        return new SendCommand(session, sessionStore, message, saver, timestamp);
    }

    private PersonalMessageCommand createSendPCommand(Session session, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String message = fieldMap.get("msg");
        String chattersName = fieldMap.get("chattersName");
        System.out.println(chattersName);
        return new PersonalMessageCommand(session, sessionStore, message, saver, timestamp, chattersName);
    }

    private ChangeIdCommand createChangeIdCommand(Session session, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String newNickname = fieldMap.get("msg");
        return new ChangeIdCommand(session, identificator, newNickname, timestamp, sessionStore, saver);
    }
}
