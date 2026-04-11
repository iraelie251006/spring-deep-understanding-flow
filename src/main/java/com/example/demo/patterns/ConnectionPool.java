package com.example.demo.patterns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// made class final to make it immutable
public final class ConnectionPool {
    private final List<String> connections;
    private int currentIndex = 0;

    private ConnectionPool() {
//        passed unmodifiableList wrapper for immutability
        connections = Collections.unmodifiableList(loadConnections());
    }
//  static inner final class Holder
    private static final class Holder {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
//        instance created only when getInstance is called
        return Holder.INSTANCE;
    }

    public String getConnection() {
//        round_robin getting connection string sequentially
        String conn = connections.get(currentIndex);
        currentIndex = (currentIndex + 1) % connections.size();
        return conn;
    }

    public void releaseConnection(String connection) {
        System.out.println("Release: " + connection);
    }

    private List<String> loadConnections() {
        List<String> conns = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            conns.add("jdbc:postgresql://db.prod.com:5432/db_" + i);
        }

        return conns;
    }
}