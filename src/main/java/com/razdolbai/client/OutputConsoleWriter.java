package com.razdolbai.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputConsoleWriter implements Runnable {
    private PrintWriter consoleOutput;
    private final BufferedReader in;

    OutputConsoleWriter(PrintWriter consoleOutput, BufferedReader in) {
        System.out.println(
                "Hello! Enter your name first via /chid [id] command.\n" +
                        "Then type /chroom [room_name] to enter the room.\n" +
                        "Type /snd [message] to send the message and /sndp [userName] [message] to send a direct one.\n" +
                        "Type /hist to view the history.\n" +
                        "Type /close to stop the work.");
        this.consoleOutput = consoleOutput;
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                processInput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void processInput() throws IOException {
        String inputData = in.readLine();
        if (inputData != null && !inputData.isEmpty()) {
            consoleOutput.println(inputData);
            consoleOutput.flush();
        }
    }
}
