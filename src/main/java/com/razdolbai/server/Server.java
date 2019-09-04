package com.razdolbai.server;

import com.razdolbai.server.commands.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService executorService;
    private final CommandFabric commandFabric;
    private final Identificator identificator;
    private Set<PrintWriter> clients;
    private ServerSocket connectionListener;

    public Server() {
        this.identificator = new Identificator();
        this.executorService = Executors.newCachedThreadPool();
        this.commandFabric = new CommandFabric(this);
        clients = new HashSet<>();
    }

    public Identificator getIdentificator() {
        return identificator;
    }

    private void startServer() {
        try {
            connectionListener = new ServerSocket(8081);
            registerShutdownHook(executorService);
            while (true) {
                startConnection(connectionListener);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerShutdownHook(ExecutorService executorService) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            if (connectionListener != null) {
                try {
                    connectionListener.close();
                    clients.forEach(PrintWriter::close);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Server closed");
        }));
    }

    private void startConnection(ServerSocket connectionListener) throws IOException {
        Socket socket = connectionListener.accept();
        executorService.execute(() -> {
            try (Socket mysocket = socket;
                    final PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            new BufferedOutputStream(
                                    mysocket.getOutputStream())));
                 final BufferedReader in = new BufferedReader(
                         new InputStreamReader(
                                 new BufferedInputStream(
                                         mysocket.getInputStream())))) {
                processClient(in, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private void processClient(BufferedReader socketIn, PrintWriter socketOut) {
        try {
            clients.add(socketOut);
            String readLine = socketIn.readLine();
            Command command = commandFabric.createCommand(readLine);
            while (!("type:/close".equals(readLine)) && !(Thread.currentThread().isInterrupted())) {
                readLine = LocalDateTime.now().toString() + " " + readLine;
                System.out.println("debug: " + readLine);
                clients.forEach(c->System.out.println(c.toString()));
                sendToAllClients(readLine);
                readLine = socketIn.readLine();
            }
            socketOut.println("Success");
            clients.remove(socketOut);
            System.out.println("client closed");
        } catch (IOException e) {
            System.out.println("Error in processClient");
            e.printStackTrace();
            socketOut.println("Error: " + e.getMessage());
        } finally {
            socketOut.flush();
        }
    }

    private void sendToAllClients(String msg) {
        clients.forEach(printWriter -> {
            printWriter.println(msg);
            printWriter.flush();
        });
    }

    public static void main(String[] args) {
        new Server().startServer();
    }
}
