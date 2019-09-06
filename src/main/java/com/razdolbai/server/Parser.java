package com.razdolbai.server;

import java.util.*;
import java.util.stream.Collectors;

class Parser {
    private final String FIELD_DELIMITER;
    private final String HEAD_BODY_DELIMITER;

    Parser(String field_delimiter, String head_body_delimiter) {
        FIELD_DELIMITER = field_delimiter;
        HEAD_BODY_DELIMITER = head_body_delimiter;
    }

    public Map<String, String> parse(String message) {
        Map<String, String>  resultMap = new HashMap<>();
        for (String mes : message.split(FIELD_DELIMITER)) {
            String[] commands = mes.split(HEAD_BODY_DELIMITER);
            resultMap.put(commands[0], commands[1]);
        }
        return resultMap;
    }
}
