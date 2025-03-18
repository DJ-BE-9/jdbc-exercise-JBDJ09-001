package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.domain.ClubStudent;
import com.nhnacademy.jdbc.club.repository.ClubRegistrationRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.PipedReader;
import java.sql.*;
import java.util.*;

@Slf4j
public class ClubRegistrationRepositoryImpl implements ClubRegistrationRepository {

    @Override
    public int save(Connection connection, String studentId, String clubId) {
        //todo#11 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        String sql = "insert into jdbc_club_registration values(?,?)";
        int resultSize = 0;


        try(PreparedStatement pstmt = connection.prepareStatement(sql)
        ){
            pstmt.setString(1, studentId);
            pstmt.setString(2, clubId);
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return resultSize;
    }

    @Override
    public int deleteByStudentIdAndClubId(Connection connection, String studentId, String clubId) {
        //todo#12 - 핵생 -> 클럽 탈퇴, executeUpdate() 결과를 반환
        String sql = "delete from jdbc_club_registration where student_id=? and club_id=?";
        int resultSize = 0;


        try(PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, clubId);
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return resultSize;
    }

    @Override
    public List<ClubStudent> findClubStudentsByStudentId(Connection connection, String studentId) {
        //todo#13 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql =
                "SELECT SC.student_id, S.name, SC.club_id, CB.club_name " +
                "FROM jdbc_students AS S " +
                "JOIN jdbc_club_registration As SC on S.id = SC.student_id " +
                "JOIN jdbc_club AS CB on SC.club_id = CB.club_id " +
                "WHERE SC.student_id=?";


        try (PreparedStatement pstmt = connection.prepareStatement(sql)
        ){
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                clubStudents.add(new ClubStudent(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
            return clubStudents;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClubStudent> findClubStudents(Connection connection) {
        //todo#21 - join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT SC.student_id, S.name, SC.club_id, C.club_name " +
                "FROM jdbc_students AS S " +
                "JOIN jdbc_club_registration AS SC ON S.id=SC.student_id " +
                "JOIN jdbc_club AS C ON SC.club_id = C.club_id ";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)
        ){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                clubStudents.add(new ClubStudent(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_left_join(Connection connection) {
        //todo#22 - left join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql =
                "SELECT S.id, S.name, SC.club_id, C.club_name " +
                "FROM jdbc_students S " +
                "LEFT JOIN jdbc_club_registration SC ON S.id = SC.student_id " +
                "LEFT JOIN jdbc_club C ON SC.club_id = C.club_id ";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                clubStudents.add(new ClubStudent(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_right_join(Connection connection) {
        //todo#23 - right join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql =
                "SELECT S.id, S.name, C.club_id, C.club_name " +
                "FROM jdbc_students AS S " +
                "RIGHT JOIN jdbc_club_registration AS SC ON S.id=SC.student_id " +
                "RIGHT JOIN jdbc_club AS C ON C.club_id=SC.club_id ";


        try (PreparedStatement pstmt = connection.prepareStatement(sql)
        ){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                clubStudents.add(new ClubStudent(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_full_join(Connection connection) {
        //todo#24 - full join = left join union right join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql =
                "SELECT S.id, S.name, C.club_id, C.club_name " +
                "FROM jdbc_students AS S " +
                "LEFT JOIN jdbc_club_registration AS SC ON S.id=SC.student_id " +
                "LEFT JOIN jdbc_club AS C ON C.club_id= SC.club_id " +
                "UNION " +
                "SELECT S.id, S.name, C.club_id, C.club_name " +
                "FROM jdbc_students AS S " +
                "RIGHT JOIN  jdbc_club_registration AS SC ON S.id=SC.student_id " +
                "RIGHT JOIN jdbc_club C ON C.club_id=SC.club_id ";


        try(PreparedStatement pstmt = connection.prepareStatement(sql)
        ){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                clubStudents.add(new ClubStudent(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_left_excluding_join(Connection connection) {
        //todo#25 - left excluding join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT S.id, S.name, C.club_id, C.club_name " +
                "FROM jdbc_students AS S " +
                "LEFT JOIN jdbc_club_registration AS SC ON S.id=SC.student_id " +
                "LEFT JOIN jdbc_club AS C ON C.club_id=SC.club_id " +
                "WHERE C.club_id is null ";


        try(PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                clubStudents.add(new ClubStudent(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_right_excluding_join(Connection connection) {
        //todo#26 - right excluding join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT S.id, S.name, C.club_id, C.club_name " +
                "FROM jdbc_students AS S " +
                "RIGHT JOIN jdbc_club_registration AS SC  ON S.id=SC.student_id " +
                "RIGHT JOIN  jdbc_club AS C ON C.club_id=SC.club_id " +
                "WHERE S.id is null ";


        try(PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                clubStudents.add(new ClubStudent(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

    @Override
    public List<ClubStudent> findClubStudents_outher_excluding_join(Connection connection) {
        //todo#27 - outher_excluding_join = left excluding join union right excluding join
        List<ClubStudent> clubStudents = new ArrayList<>();
        String sql = "SELECT S.id, S.name, C.club_id, C.club_name " +
                "FROM jdbc_students AS S " +
                "LEFT JOIN jdbc_club_registration AS SC ON S.id=SC.student_id " +
                "LEFT JOIN  jdbc_club AS C ON C.club_id=SC.club_id " +
                "WHERE C.club_id is null " +
                "UNION " +
                "SELECT S.id, S.name, C.club_id, C.club_name " +
                "FROM jdbc_students AS S " +
                "RIGHT JOIN jdbc_club_registration AS SC  ON S.id=SC.student_id " +
                "RIGHT JOIN  jdbc_club AS C ON C.club_id=SC.club_id " +
                "WHERE S.id is null ";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                clubStudents.add(new ClubStudent(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clubStudents;
    }

}