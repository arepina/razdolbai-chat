package com.razdolbai.client;

import com.razdolbai.common.CommandType;

class InputParser {

    Command parse(String input) {
        String message = "";
        String chattersName = "";
        String commandType;
        String[] mess = input.split(" ");
        commandType = mess[0];
        if (mess.length == 2) {
            message = mess[1];
        }
        else if (mess.length == 3) {
            chattersName = mess[1];
            message = mess[2];
        }
        CommandType command = CommandType.fromString(commandType);
        if (command.equals(CommandType.UNKNOWN)) {
            System.out.println("Unknown command: " + commandType);
            return null;
        }

        if (message.length() > 149) {
            System.out.println("Message is too long, try again");
            return null;
        }

        if (chattersName.equals(""))
            return new Command(command, message);
        return new Command(command, message, chattersName);
    }
}
