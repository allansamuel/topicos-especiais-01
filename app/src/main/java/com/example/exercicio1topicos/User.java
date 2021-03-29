package com.example.exercicio1topicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {
    private long id;
    private String name;
    private String email;
    private String phone;
    private Date birthday;
    private String gender;
    private ArrayList<String> hobbies;

    public User() {
    }

    public User(long id, String name, String email, String phone, Date birthday, String gender, ArrayList<String> hobbies) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
        this.hobbies = hobbies;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(ArrayList<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return name + " : " + gender;
    }
}
