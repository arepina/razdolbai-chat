package com.razdolbai.client;

import com.razdolbai.common.CommandType;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.mockito.Mockito.*;

public class InputConsoleTest {

    private CommandSender commandSenderMock = mock(CommandSender.class);
    private BufferedReader readerMock = mock(BufferedReader.class);
    private InputParser inputParserMock = mock(InputParser.class);
    private InputConsole sut;

    @Before
    public void setUp() {

        sut = new InputConsole(commandSenderMock, readerMock, inputParserMock);

    }

    @Test
    public void test() throws IOException {

        when(readerMock.readLine()).thenReturn(CommandType.SEND.getValue() + " testtest");
        Command command = new Command(CommandType.SEND, "testtest");
        when(inputParserMock.parse(CommandType.SEND.getValue() + " testtest")).thenReturn(command);
        doNothing().when(commandSenderMock).send(command);

        sut.readCommand();

        verify(commandSenderMock).send(command);
        verify(inputParserMock).parse(CommandType.SEND.getValue() + " testtest");


    }

    @Test()
    public void shouldNotThrowExceptionWhenReadLineThrowsException() throws IOException {
        when(readerMock.readLine()).thenThrow(new IOException());

        sut.readCommand();
    }
}