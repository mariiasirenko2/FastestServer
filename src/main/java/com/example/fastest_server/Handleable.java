package com.example.fastest_server;

public interface Handleable {
    default void handleException(Exception e) {
        System.out.println("An unknown error occurred");
    }
}
