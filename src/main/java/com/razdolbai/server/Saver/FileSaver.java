package com.razdolbai.server.Saver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class FileSaver implements Saver {

    protected String filename = "";

    protected PrintWriter out;
    protected boolean isClosed = false;

    public FileSaver (String filename) throws IOException {
        this.filename = filename;
        open();
    }


    protected void open() throws  IOException{
        out = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                new FileOutputStream(filename, true))));

    }

    @Override
    public synchronized void save(String string, LocalDateTime dateTime) throws IOException{
        out.println("[" + dateTime.toString() + "]" + string);
        out.flush();
    }

    public synchronized void close() {
        if (isClosed) return;

        isClosed = true;

        out.flush();
        out.close();
    }
}
