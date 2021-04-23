package com.example.exercicio1topicos;

import java.io.Serializable;

public class Customer implements Serializable {
    private int id;
    private String name;
    private String phoneNumber;
    private long birthDateInMillis;
    private boolean blackList;
    private String creationTimestamp;

    public Customer() {
    }

    public Customer(int id, String name, String phoneNumber, long birthDateInMillis, boolean blackList, String creationTimestamp) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDateInMillis = birthDateInMillis;
        this.blackList = blackList;
        this.creationTimestamp = creationTimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public String toString() {
        return name;
    }
}
