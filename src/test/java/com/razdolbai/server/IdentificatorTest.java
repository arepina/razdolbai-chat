package com.razdolbai.server;

import com.razdolbai.server.exceptions.OccupiedNicknameException;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class IdentificatorTest {
    private Identificator identificator = new Identificator();

    @Test
    public void shouldAddNickNameWhenNickNameReceivedAndOldIsNull() throws OccupiedNicknameException {
        String oldNick = null;
        String newNick = "newNick";
        identificator.changeNickname(oldNick, newNick);
        HashMap<String, String> nicknames = (HashMap<String, String>) Whitebox.getInternalState(identificator, "nicknames");
        assertEquals(nicknames.size(), 1);
        assertTrue(nicknames.containsKey(newNick));
        assertFalse(nicknames.containsKey(oldNick));
    }

    @Test
    public void shouldAddNewNickNameAndRemoveOldWhenNickNameChanged() throws OccupiedNicknameException {
        String oldNick = "oldNick";
        String newNick = "newNick";
        identificator.changeNickname(oldNick, newNick);
        HashMap<String, String> nicknames = (HashMap<String, String>) Whitebox.getInternalState(identificator, "nicknames");
        assertEquals(nicknames.size(), 1);
        assertTrue(nicknames.containsKey(newNick));
        assertFalse(nicknames.containsKey(oldNick));
    }

    @Test(expected = OccupiedNicknameException.class)
    public void shouldThrowsOccupiedNicknameExceptionWhenNickNameIsAlreadyInSet() throws OccupiedNicknameException {
        String oldNick = "oldNick";
        String newNick = "newNick";
        String room = "room";
        HashMap<String, String> nicknames = (HashMap<String, String>) Whitebox.getInternalState(identificator, "nicknames");
        nicknames.put(newNick, room);
        Whitebox.setInternalState(identificator, "nicknames", nicknames);

        identificator.changeNickname(oldNick, newNick);
        HashMap<String, String> nicknamesNew = (HashMap<String, String>) Whitebox.getInternalState(identificator, "nicknames");
        assertEquals(nicknames.size(), 1);
        assertTrue(nicknamesNew.containsKey(newNick));
    }
}
