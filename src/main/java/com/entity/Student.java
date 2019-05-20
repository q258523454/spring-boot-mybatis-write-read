package com.entity;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Student {
    private Long id;
    private String username;
    private String password;
    private Date regTime;

    public Student() {
    }

    public Student(String username, String password, Date regTime) {
        this.username = username;
        this.password = password;
        this.regTime = regTime;
    }

    public static Student createStudent() {
        Random random = new Random();
        String passwd = "";
        for (int i = 0; i < 6; i++) {
            passwd += Integer.toString(random.nextInt(10));
        }
        int pwd = new Integer(passwd);
        String username = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5);
        return new Student(username, passwd, new Date());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

}
