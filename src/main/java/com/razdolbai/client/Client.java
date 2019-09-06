package com.razdolbai.client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Client {
    public static void main(String[] args) {
        try (
                final Socket socket = new Socket("localhost", 8082);
                final PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(socket.getOutputStream())));
                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(socket.getInputStream())));
                final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                final ServerSocket connectionListener = new ServerSocket(Integer.parseInt(args[0]));
                final Socket server = connectionListener.accept();
                final PrintWriter consoleOutput = new PrintWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(
                                        server.getOutputStream())))
        ) {
            Thread thread = new Thread(new OutputConsoleWriter(consoleOutput, in));
            thread.start();

            CommandSender commandSender = new CommandSender(out, new SystemExit());
            InputConsole inputConsole = new InputConsole(commandSender, reader, new InputParser());

            while (!Thread.currentThread().isInterrupted()) {
                inputConsole.readCommand();
            }
            consoleOutput.println("CLOSE");
            consoleOutput.flush();
            thread.interrupt();

        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
