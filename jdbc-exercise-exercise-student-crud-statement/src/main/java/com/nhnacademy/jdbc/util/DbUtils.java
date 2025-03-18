package com.nhnacademy.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbUtils {
    public DbUtils(){
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            //todo connection.
//            String url = "jdbc:mysql://ip:3306/jdbc";
            String url = "jdbc:mysql://localhost:3306/jdbc";
                String user = "root";
            String password = "Zcbnm147!@";
            connection = DriverManager.getConnection(url,user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}