package com.example.fastest_server.answer;

import java.util.HashMap;
import java.util.Map;

public enum Chars {
    EMPTY(0),
    A(1),
    B(2),
    C(3),
    D(4);
    private int value;
    private static Map map = new HashMap<>();

    private Chars(int value) {
        this.value = value;
    }

    static {
        for (Chars answer : Chars.values()) {
            map.put(answer.value, answer);
        }
    }
    public static Chars valueOf(int answer) {
        return (Chars) map.get(answer);
    }
}
