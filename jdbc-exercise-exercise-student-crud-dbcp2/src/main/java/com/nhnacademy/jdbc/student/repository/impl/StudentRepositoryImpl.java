package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import java.sql.*;
import java.util.Optional;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public int save(Connection connection, Student student){
        //todo#2 학생등록
        String sql = "insert into jdbc_datasource_students (id, name,gender, age) values(?,?,?,?)";
        PreparedStatement pstmt = null;
        int resultSize;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, student.getId());
            pstmt.setString(2, student.getName());
            if(student.getGender().equals(Student.GENDER.F)){
                pstmt.setString(3, "F");
            }else{
                pstmt.setString(3, "M");
            }
            pstmt.setInt(4, student.getAge());
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSize;
    }

    @Override
    public Optional<Student> findById(Connection connection,String id){
        //todo#3 학생조회
        String sql = "select * from  jdbc_datasource_students where id=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if(rs.next()){
                String userId = rs.getString(1);
                String userName = rs.getString(2);
                String userGender = rs.getString(3);
                int userAge = rs.getInt(4);

                Student student = null;
                if(userGender.equals("F")){
                     student = new Student(userId,userName,Student.GENDER.F,userAge);
                }else{
                     student = new Student(userId,userName,Student.GENDER.M,userAge);
                }
                return Optional.of(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public int update(Connection connection,Student student){
        //todo#4 학생수정
        String sql = "update jdbc_datasource_students set name=?, gender=?, age=? where id=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int resultSize;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            if(student.getGender().equals("M")){
                pstmt.setString(2, "M");
            }else{
                pstmt.setString(2, "F");
            }
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getId());
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSize;
    }

    @Override
    public int deleteById(Connection connection,String id){
        //todo#5 학생삭제
        String sql = "delete from jdbc_datasource_students where id=?";
        PreparedStatement pstmt = null;
        int resultSize;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSize;
    }

}