package com.nhnacademy.jdbc.bank.repository.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    public Optional<Account> findByAccountNumber(Connection connection, long accountNumber){
        //todo#1 계좌-조회
        String sql = "select * from jdbc_account where account_number=?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, accountNumber);
            rs = pstmt.executeQuery();

            if(rs.next()){
                long accountNo = rs.getLong(1);
                String name = rs.getString(2);
                long balance = rs.getInt(3);
                Account account = new Account(accountNo, name, balance);
                return Optional.of(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Account account) {
        //todo#2 계좌-등록, executeUpdate() 결과를 반환 합니다.
        String sql = "insert into jdbc_account values(?,?,?)";

        PreparedStatement pstmt = null;
        int resultSize = 0;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1,account.getAccountNumber());
            pstmt.setString(2,account.getName());
            pstmt.setLong(3,account.getBalance());
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return resultSize;
    }

    @Override
    public int countByAccountNumber(Connection connection, long accountNumber){
        int count=0;
        //todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환
        String sql = "select count(*) from jdbc_account where account_number=?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1,accountNumber);
            rs = pstmt.executeQuery();

            if(rs.next()){
                count = (int) rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return count;
    }

    @Override
    public int deposit(Connection connection, long accountNumber, long amount){
        //todo#4 입금, executeUpdate() 결과를 반환 합니다.
        String sql = "update jdbc_account set balance=? where account_number=?";

        Account findAccount = findByAccountNumber(connection, accountNumber).get();
        PreparedStatement pstmt = null;
        int resultSize = 0;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1,findAccount.getBalance()+amount);
            pstmt.setLong(2,accountNumber);
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return resultSize;
    }

    @Override
    public int withdraw(Connection connection, long accountNumber, long amount){
        //todo#5 출금, executeUpdate() 결과를 반환 합니다.
        String sql = "update jdbc_account set balance=? where account_number=?";

        Account account = findByAccountNumber(connection, accountNumber).get();
        PreparedStatement pstmt = null;
        int resultSize = 0;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1,account.getBalance()-amount);
            pstmt.setLong(2, accountNumber);

            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return resultSize;
    }

    @Override
    public int deleteByAccountNumber(Connection connection, long accountNumber) {
        //todo#6 계좌 삭제, executeUpdate() 결과를 반환 합니다.
        String sql = "delete from jdbc_account where account_number=?";

        PreparedStatement pstmt = null;
        int resultSize = 0;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, accountNumber);
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return resultSize;
    }
}
