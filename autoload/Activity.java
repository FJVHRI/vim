// src/com/campus/entity/Activity.java
package com.campus.entity;
import java.time.LocalDateTime;

public class Activity {
    private int id;
    private String name;
    private String info;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private int maxNum;
    private String status;
    private int adminId;
    // 构造 + getter / setter 同上，这里只列关键
    public Activity(){}
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public LocalDateTime getStartTime(){return startTime;}
    public void setStartTime(LocalDateTime t){this.startTime=t;}
    public LocalDateTime getEndTime(){return endTime;}
    public void setEndTime(LocalDateTime t){this.endTime=t;}
    public String getLocation(){return location;}
    public void setLocation(String l){this.location=l;}
    public int getMaxNum(){return maxNum;}
    public void setMaxNum(int m){this.maxNum=m;}
    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}
    public int getAdminId(){return adminId;}
    public void setAdminId(int a){this.adminId=a;} 
public String getInfo(){ return info; }
public void setInfo(String info){ this.info = info; }

 }
