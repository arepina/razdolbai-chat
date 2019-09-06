package com.razdolbai.client;


import com.razdolbai.common.CommandType;

import java.io.PrintWriter;

class CommandSender {
    private static final String DELIMITER = "\0";

    private PrintWriter out;
    private SystemExit systemExit;

    CommandSender(PrintWriter out, SystemExit systemExit) {
        this.out = out;
        this.systemExit = systemExit;
    }

    void send(Command command) {
        String result = "type:" + command.getType().getValue();
        if (!command.getMessage().isEmpty()) {
            result = addMessage(command.getMessage(), result, "msg:");
        }
        if (command.getChattersName()!=null && !command.getChattersName().isEmpty()) {
            result = addMessage(command.getChattersName(), result, "chattersName:");
        }
        out.println(result);
        out.flush();
        if (command.getType() == CommandType.CLOSE) {
            systemExit.exit();
        }
    }

    private String addMessage(String info, String message, String type) {
        String result = message;
        result += DELIMITER;
        result += type + info;
        return result;
    }

}
