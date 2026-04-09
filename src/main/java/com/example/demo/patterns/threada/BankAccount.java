package com.example.demo.patterns.threada;

class BankAccount {
    private double balance;
    private final Object lock = new Object();
    public void transfer(BankAccount to, double amount) throws InterruptedException {
        synchronized(lock) {
            while (balance < amount) {
                lock.wait();
            }
            balance -= amount; to.deposit(amount);
        }
    }
    public void deposit(double amount) {
        synchronized(lock) {
            balance += amount;
            lock.notifyAll();
        }
    }
}

