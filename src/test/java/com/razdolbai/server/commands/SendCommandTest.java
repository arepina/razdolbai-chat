package com.razdolbai.server.commands;

import com.razdolbai.server.exceptions.UnidentifiedRoomException;
import com.razdolbai.server.history.saver.Saver;
import com.razdolbai.server.Session;
import com.razdolbai.server.SessionStore;
import com.razdolbai.server.exceptions.UnidentifiedUserException;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendCommandTest {
    private Saver mockSaver;
    private Session mockSession;
    private SessionStore mockSessionStore;
    private LocalDateTime timestamp;
    private String username;
    private String room;

    @Before
    public void setup() {
        mockSaver = mock(Saver.class);
        mockSession = mock(Session.class);
        mockSessionStore = mock(SessionStore.class);
        timestamp = LocalDateTime.now();
        username = "James";
        room = "testroom";
    }

    @Test(expected = UnidentifiedRoomException.class)
    public void shouldThrowUnidefinedRoomException() throws UnidentifiedUserException, IOException, UnidentifiedRoomException {
        final String message = "HI WORLD!";
        final String decoratedMessage = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + username + ": " + message;
        SendCommand testSendCommand = new SendCommand(mockSession, mockSessionStore, message, mockSaver, timestamp);
        when(mockSession.getUsername()).thenReturn(username);
        testSendCommand.execute();
    }

    @Test

    public void shouldSendCommand() throws UnidentifiedUserException, IOException, UnidentifiedRoomException {
        final String message = "HI WORLD!";
        final String decoratedMessage = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + username + ": " + room + ": " + message;
        SendCommand testSendCommand = new SendCommand(mockSession, mockSessionStore, message, mockSaver, timestamp);
        when(mockSession.getUsername()).thenReturn(username);
        when(mockSession.getRoom()).thenReturn(room);
        testSendCommand.execute();
        verify(mockSession, times(2)).getUsername();
        verify(mockSessionStore).sendToAll(decoratedMessage);
        verify(mockSaver).save(decoratedMessage, timestamp);
    }

    @Test(expected = UnidentifiedUserException.class)
    public void shouldThrowUnidentifiedException() throws UnidentifiedUserException, IOException, UnidentifiedRoomException {
        Saver mockSaver = mock(Saver.class);
        Session mockSession = mock(Session.class);
        SessionStore mockSessionStore = mock(SessionStore.class);
        final LocalDateTime timestamp = LocalDateTime.now();
        SendCommand testSendCommand = new SendCommand(mockSession, mockSessionStore, "", mockSaver, timestamp);
        when(mockSession.getUsername()).thenReturn(null);
        testSendCommand.execute();
    }
}
