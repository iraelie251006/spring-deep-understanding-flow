package com.example.demo.patterns.threada;

class BankAccount {
    private double balance;
    private final Object lock = new Object();
    public void transfer(BankAccount to, double amt) throws InterruptedException {
        synchronized(lock) { // acquire monitor
            while (balance < amt) {
                lock.wait(); // RELEASES lock, parks here
            }
            balance -= amt; to.deposit(amt);
        } // releases lock on exit
    }
    public void deposit(double amt) {
        synchronized(lock) { // acquire monitor
            balance += amt;
            lock.notifyAll(); // wake up waiters
        } // releases lock on exit
    }
}

