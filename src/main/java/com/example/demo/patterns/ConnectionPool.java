package com.example.demo.patterns;

import java.util.ArrayList;
import java.util.List;

public final class ConnectionPool {

    private ConnectionPool () {
        List<String> connections = loadConnections();
    }

    private static final class Holder {
        static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
        return Holder.INSTANCE;
    }

    public String getConnection() {
        List<String> conns = loadConnections();
        for (int i = 0; i <= conns.size(); i++) {
            return conns.get(i);
        }
    }

    public void releaseConnection(String connection) {
        System.out.println("Released");
    }

    private List<String> loadConnections() {
        List<String> conns = new ArrayList<String>();
        conns.add("conn1");
        conns.add("conn2");
        conns.add("conn3");
        conns.add("conn4");
        conns.add("conn5");
        conns.add("conn6");
        conns.add("conn7");
        conns.add("conn8");
        conns.add("conn9");
        conns.add("conn10");

        return conns;
    }
}
