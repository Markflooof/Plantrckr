package com.example.plantrckr;

public class User {
    private String userId;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String contact;
    private String address;
    private String birthday;

    private double balance;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String userId, String email, String password, String firstname , String lastname, String contact , String address, String birthday, double balance) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contact = contact;
        this.address = address;
        this.birthday = birthday;
        this.balance = balance;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public double getBalance() { return balance; }


    public void setUserId() {
        this.userId = userId;
    }

    public void setEmail() {
        this.email = email;
    }

    public void setPassword() {
        this.password = password;
    }

    public void setFirstname() {
        this.firstname = firstname;
    }

    public void setLastname() {
        this.lastname = lastname;
    }

    public void setContact() {
        this.contact = contact;
    }

    public void setAddress() {
        this.address = address;
    }

    public void setBirthday() {
        this.birthday = birthday;
    }



}
