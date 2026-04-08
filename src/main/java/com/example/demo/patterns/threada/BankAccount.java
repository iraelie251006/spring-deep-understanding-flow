package com.example.demo.patterns.threada;

class BankAccount {
    private double balance;
    private final Object lock = new Object();
}
