package com.example.exercicio1topicos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Customer implements Serializable {
    private long id;
    private String name;
    private String email;
    private String phoneNumber;
    private long birthDateInMillis;
    private boolean blackList;
    private Timestamp creationTimestamp;

    public Customer() {
    }

    public Customer(long id, String name, String email, String phoneNumber, long birthDateInMillis, boolean blackList, Timestamp creationTimestamp) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDateInMillis = birthDateInMillis;
        this.blackList = blackList;
        this.creationTimestamp = creationTimestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getBirthDateInMillis() {
        return birthDateInMillis;
    }

    public void setBirthDateInMillis(long birthDateInMillis) {
        this.birthDateInMillis = birthDateInMillis;
    }

    public boolean isBlackList() {
        return blackList;
    }

    public void setBlackList(boolean blackList) {
        this.blackList = blackList;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public String toString() {
        return name;
    }
}
