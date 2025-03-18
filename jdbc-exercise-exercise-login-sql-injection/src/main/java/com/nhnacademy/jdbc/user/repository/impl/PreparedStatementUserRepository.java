package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Optional;

@Slf4j
public class PreparedStatementUserRepository implements UserRepository {
    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        //todo#11 -PreparedStatement- 아이디 , 비밀번호가 일치하는 회원조회
        String sql = "select * from jdbc_users where user_id=? AND user_password=?";

        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, userPassword);
            rs = pstmt.executeQuery();

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
        //todo#12-PreparedStatement-회원조회
        String sql = "select * from jdbc_users where user_id=?";

        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

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
        //todo#13-PreparedStatement-회원저장
        String sql = "insert into jdbc_users values(?,?,?)";

        int resultSize = 0;
        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getUserPassword());
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        //todo#14-PreparedStatement-회원정보 수정
        String sql = "update jdbc_users set user_password=? where user_id=?";

        int resultSize;
        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;

        try {

            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, userPassword);
            pstmt.setString(2, userId);
            resultSize = pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

    @Override
    public int deleteByUserId(String userId) {
        //todo#15-PreparedStatement-회원삭제
        String sql = "delete from jdbc_users where user_id=? ";

        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;
        int resultSize = 0;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, userId);
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }
}
