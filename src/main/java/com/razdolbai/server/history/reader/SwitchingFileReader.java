package com.razdolbai.server.history.reader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SwitchingFileReader implements Reader {
    private static final int initialCapacity = 2097152 * 5;
    //About half of a day

    @Override
    public List<String> getHistory() {
        ArrayList<String> res  = new ArrayList<>(initialCapacity);
        readAllLinesDFS(new File("./resources/History"), res);
        return res;
    }

    static void readAllLinesDFS(File folder, List<String> res) {
        for(File f : folder.listFiles()) {
            if(f.isDirectory()) {
                readAllLinesDFS(f, res);
            }

            if (f.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ( (line = reader.readLine()) != null ) {
                        res.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}