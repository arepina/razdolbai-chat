package com.razdolbai.client;


import com.razdolbai.common.CommandType;

class Command {
    private CommandType commandType;
    private String message;
    private String chattersName;

    Command(CommandType commandType, String message) {
        this.commandType = commandType;
        this.message = message;
    }

    Command(CommandType commandType, String message, String chattersName) {
        this.chattersName = chattersName;
        this.commandType = commandType;
        this.message = message;
    }

    String getChattersName() { return chattersName; }

    CommandType getType() {
        return commandType;
    }

    String getMessage() {
        return message;
    }
}
