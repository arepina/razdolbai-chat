package com.razdolbai.server;

import com.razdolbai.server.exceptions.OccupiedNicknameException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Identificator {
    private Map<String, String> nicknames;
    private final Lock lock;

    Identificator() {
        nicknames = new HashMap<>(1500);
        lock = new ReentrantLock();
    }

    public Map<String, String> getNicknames() {
        return nicknames;
    }

    public synchronized void changeNickname(String oldNickname, String newNickname) throws OccupiedNicknameException {
        try {
            lock.lock();
            if (nicknames.containsKey(newNickname)) {
                throw new OccupiedNicknameException();
            }
            nicknames.put(newNickname, nicknames.get(oldNickname));
            if (oldNickname != null) {
                nicknames.remove(oldNickname);
            }
        } finally {
            lock.unlock();
        }
    }

    public synchronized void changeRoom(String nickname, String newRoom) throws OccupiedNicknameException {
        try {
            lock.lock();
            if (!nicknames.containsKey(nickname)) {
                throw new OccupiedNicknameException();
            }
            nicknames.put(nickname, newRoom);
        } finally {
            lock.unlock();
        }
    }
}

