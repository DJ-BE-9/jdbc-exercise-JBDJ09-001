package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.repository.ClubRepository;

import java.io.PipedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public class ClubRepositoryImpl implements ClubRepository {

    @Override
    public Optional<Club> findByClubId(Connection connection, String clubId) {
        //todo#3 club 조회
        String sql = "select * from jdbc_club where club_id=?";

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql);
        ) {
            pstmt.setString(1, clubId);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                String club_id = rs.getString(1);
                String club_name = rs.getString(2);
                LocalDateTime club_create_at = (LocalDateTime) rs.getObject(3);
                Club club = new Club(club_id, club_name, club_create_at);
                return Optional.of(club);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Club club) {
        //todo#4 club 생성, executeUpdate() 결과를 반환
        String sql = "insert into jdbc_club (club_id, club_name) values(?,?)";
        int resultSize = 0;
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1,club.getClubId());
            pstmt.setString(2, club.getClubName());
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSize;
    }

    @Override
    public int update(Connection connection, Club club) {
        //todo#5 club 수정, clubName을 수정합니다. executeUpdate()결과를 반환
        String sql = "update jdbc_club set club_name=? where club_id=?";
        int resultSize = 0;
        try(PreparedStatement pstmt = connection.prepareStatement(sql)
        ) {
            pstmt.setString(1, club.getClubName());
            pstmt.setString(2, club.getClubId());
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSize;
    }

    @Override
    public int deleteByClubId(Connection connection, String clubId) {
        //todo#6 club 삭제, executeUpdate()결과 반환
        String sql = "delete from jdbc_club where club_id=?";
        int resultSize = 0;
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, clubId);
            resultSize = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSize;
    }

    @Override
    public int countByClubId(Connection connection, String clubId) {
        //todo#7 clubId에 해당하는 club의 count를 반환
        String sql = "select count(*) from jdbc_club where club_id=?";
        int count = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, clubId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
}
