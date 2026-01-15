// src/com/campus/entity/Apply.java
package com.campus.entity;
import java.time.LocalDateTime;

public class Apply {
    private int id;
    private int stuId;
    private int actId;
    private LocalDateTime applyTime;
    private LocalDateTime checkTime;
    private Integer score;
    private String comment;
    // 构造 + getter / setter
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public int getStuId(){return stuId;}
    public void setStuId(int s){this.stuId=s;}
    public int getActId(){return actId;}
    public void setActId(int a){this.actId=a;}
    public LocalDateTime getApplyTime(){return applyTime;}
    public void setApplyTime(LocalDateTime t){this.applyTime=t;}
    public LocalDateTime getCheckTime(){return checkTime;}
    public void setCheckTime(LocalDateTime t){this.checkTime=t;}
    public Integer getScore(){return score;}
    public void setScore(Integer s){this.score=s;}
    public String getComment(){return comment;}
    public void setComment(String c){this.comment=c;}
}
