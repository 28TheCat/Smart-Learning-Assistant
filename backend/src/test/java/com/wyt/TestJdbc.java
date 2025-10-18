package com.wyt;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJdbc {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://192.168.100.128:3306/tlias?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "1234";

        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("连接成功: " + conn);
        conn.close();
    }
}
