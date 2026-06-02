package com.template.model.dao;

import com.template.model.ConnectionFactory;
import com.template.model.dto.TeamDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {
    // CREATE
    public void create(TeamDTO team) {
        // O ? são placeholders do PreparedStatement que representão os valores que serão recebidos em tempo de execução do código.
        String sql = "INSERT INTO \"Times\" (nome, cidade, estadio, liga) VALUES (?, ?, ?, ?)";
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, team.getName());
            stmt.setString(2, team.getCity());
            stmt.setString(3, team.getStadium());
            stmt.setString(4, team.getLeague());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting team: " + e.getMessage());
        }
    }

    // READ
    public List<TeamDTO> findAll() {
        List<TeamDTO> teams = new ArrayList<>();
        String sql = "SELECT * FROM \"Times\"";
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                TeamDTO team = new TeamDTO();

                team.setId(rs.getInt("id"));
                team.setName(rs.getString("nome"));
                team.setCity(rs.getString("cidade"));
                team.setStadium(rs.getString("estadio"));
                team.setLeague(rs.getString("liga"));

                teams.add(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching teams: " + e.getMessage());
        }

        return teams;
    }

    // UPDATE
    public void update(TeamDTO team) {
        String sql = "UPDATE \"Times\" SET nome=?, cidade=?, estadio=?, liga=? WHERE id=?";
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, team.getName());
            stmt.setString(2, team.getCity());
            stmt.setString(3, team.getStadium());
            stmt.setString(4, team.getLeague());
            stmt.setInt(5, team.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating team: " + e.getMessage());
        }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM \"Times\" WHERE id = ?";
        try (
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting team: " + e.getMessage());
        }
    }
}