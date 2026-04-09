package com.example.demo.patterns.threada;

class BankAccount {
    private double balance;
    private final Object lock = new Object();
    public void transfer(BankAccount to, double amount) throws InterruptedException {
        synchronized(lock) { // acquire monitor
            while (balance < amount) {
                lock.wait(); // RELEASES lock, parks here
            }
            balance -= amount; to.deposit(amount);
        } // releases lock on exit
    }
    public void deposit(double amount) {
        synchronized(lock) { // acquire monitor
            balance += amount;
            lock.notifyAll();
        }
    }
}

