package com.example.fastest_server.test;

public class TestNameId {

    //TODO :Это БОЛЬШУЩИЙ КОСТЫЛЬ
    private String name;
    private int id;

    public TestNameId(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
