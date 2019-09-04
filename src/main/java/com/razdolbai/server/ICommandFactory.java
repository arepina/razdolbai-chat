package com.razdolbai.server;

import com.razdolbai.server.commands.Command;

public interface ICommandFactory {
    Command getCommand(Session senderSession, String message, String timeStamp);
}
