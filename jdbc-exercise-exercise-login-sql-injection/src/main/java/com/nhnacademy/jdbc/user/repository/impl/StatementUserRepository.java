package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class StatementUserRepository implements UserRepository {

    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        //todo#1 아이디, 비밀번호가 일치하는 User 조회
        String sql = String.format("select * from jdbc_users where user_id='%s' AND user_password='%s'",userId,userPassword);

        Connection connection = DbUtils.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            if(rs.next()){
                String id = rs.getString(1);
                String name = rs.getString(2);
                String password = rs.getString(3);
                User user = new User(id, name, password);
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String userId) {
        if(Objects.isNull(userId)){
            throw new IllegalArgumentException();
        }

        String sql = String.format("select * from jdbc_users where user_id= '%s'",userId);
        Connection connection = DbUtils.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            if(rs.next()){
                String id = rs.getString(1);
                String name = rs.getString(2);
                String password = rs.getString(3);
                User user = new User(id, name, password);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public int save(User user) {
        //todo#3- User 저장
        String sql = String.format("insert into jdbc_users values('%s','%s','%s')",
                user.getUserId(),
                user.getUserName(),
                user.getUserPassword());

        int resultSize = 0;
        Connection connection = DbUtils.getConnection();
        Statement stmt = null;

        try {
            stmt = connection.createStatement();
            resultSize = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        //todo#4-User 비밀번호 변경
        String sql = String.format("update jdbc_users set user_password='%s' where user_id='%s'",userPassword,userId);

        int resultSize;
        Connection connection = DbUtils.getConnection();
        Statement stmt = null;

        try {

            stmt = connection.createStatement();
            resultSize = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

    @Override
    public int deleteByUserId(String userId) {
        //todo#5 - User 삭제
        String sql = String.format("delete from jdbc_users where user_id='%s'",userId);

        Connection connection = DbUtils.getConnection();
        Statement stmt = null;
        int resultSize = 0;

        try {
            stmt = connection.createStatement();
            resultSize = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

}
