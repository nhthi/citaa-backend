package com.citaa.citaa.model;

public enum Role {
    ADMIN(1,"Admin"),
    STARTUP(1,"Startup"),
    INVESTOR(1,"Investor"),
    EXPERT(1,"Expert")
    ;
    int id;
    String name;

    Role(int id, String name){
        this.id = id;
        this.name = name;
    }
}
