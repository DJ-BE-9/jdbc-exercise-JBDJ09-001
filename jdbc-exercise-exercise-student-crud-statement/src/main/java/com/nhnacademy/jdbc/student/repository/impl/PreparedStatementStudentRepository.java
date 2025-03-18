package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {
    @Override
    public int save(Student student){
        //todo#1 insert student
        String id = student.getId();
        String name = student.getName();
        Student.GENDER gender = student.getGender();
        int age = student.getAge();
        LocalDateTime createAtTime = student.getCreatedAt();
        String sql ="INSERT INTO jdbc_students  (id, name, gender, age) VALUES (?, ?, ?, ?)";

        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;
        int resultSize = 0;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            if(gender.equals(Student.GENDER.M)){
                pstmt.setString(3, "M");
            }else{
                pstmt.setString(3, "F");
            }
            pstmt.setInt(4, age);
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 student 조회
        String sql = "SELECT * FROM jdbc_students WHERE id=?";

        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if(rs.next()){
                String sId = rs.getString(1);
                String sName = rs.getString(2);
                String sGender = rs.getString(3);
                int sAge = rs.getInt(4);
                Student student = null;
                if(sGender.equals("M")){
                    student = new Student(sId,sName, Student.GENDER.M,sAge);
                }else{
                    student = new Student(sId,sName,Student.GENDER.F,sAge);
                }
                return Optional.of(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int update(Student student){
        //todo#3 student 수정, name <- 수정합니다.
        String sql = "UPDATE jdbc_students set name = ?, gender=?, age=? WHERE id = ? ";

        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int resultSize = 0;

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
            log.debug("resultSize={}", resultSize);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

    @Override
    public int deleteById(String id){
       //todo#4 student 삭제
        String sql = "DELETE FROM jdbc_students WHERE id = ?";

        Connection connection = DbUtils.getConnection();
        PreparedStatement pstmt = null;
        int resultSize = 0;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            resultSize = pstmt.executeUpdate();
            log.debug("resultSize={}", resultSize);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

}
