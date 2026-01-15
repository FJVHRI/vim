// src/com/campus/entity/User.java
package com.campus.entity;

public class User {
    private int id;
    private String account;
    private String name;
    private String role;   // student / admin  

    public User(){}
    // 全参构造 + getter / setter 自己补，下面用 Lombok 风格示例
    public User(int id, String account, String name, String role){
        this.id=id;this.account=account;this.name=name;this.role=role;
    }
    public int getId(){return id;}
    public String getName(){return name;}
    public String getRole(){return role;}
    public String getAccount(){return account;}
}
