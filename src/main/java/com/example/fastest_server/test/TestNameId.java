package com.example.fastest_server.test;

public class TestNameId {

    //TODO :Это БОЛЬШУЩИЙ КОСТЫЛЬ
    private Long id;
    private String name;


    public TestNameId(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
