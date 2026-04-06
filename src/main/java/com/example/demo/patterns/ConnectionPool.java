package com.example.demo.patterns;

public final class ConnectionPool {
    private ConnectionPool() {}

    private static final class Holder {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return Holder.INSTANCE;
    }
}