package com.razdolbai.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputConsoleWriter implements Runnable {
    private PrintWriter consoleOutput;
    private final BufferedReader in;

    OutputConsoleWriter(PrintWriter consoleOutput, BufferedReader in) {
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
