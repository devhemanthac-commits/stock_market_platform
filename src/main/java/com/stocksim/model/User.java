package com.stocksim.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private double balance;

    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.balance = 1000000.00; // ₹10,00,000 demo funds
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
