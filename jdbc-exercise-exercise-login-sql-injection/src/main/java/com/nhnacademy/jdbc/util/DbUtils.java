package com.nhnacademy.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    public DbUtils(){
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            //todo#0 {ip},{database},{username},{password} 설정합니다.
            String url = "jdbc:mysql://localhost:3306/jdbc";
            String name = "root";
            String password = "Zcbnm147!@";
            connection = DriverManager.getConnection(url,name,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return connection;
    }

}