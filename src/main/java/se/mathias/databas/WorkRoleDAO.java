package se.mathias.databas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkRoleDAO {
    private final Connection connection;

    public WorkRoleDAO(Connection connection) {
        this.connection = connection;
    }

    // Insertar en work role
    public void insertWorkRole(WorkRole workRole) throws SQLException {
        String query = "INSERT INTO work_role (title, description, salary, creation_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, workRole.getTitle());
            stmt.setString(2, workRole.getDescription());
            stmt.setDouble(3, workRole.getSalary());

            // Ser till att databasen inte får oväntat null i creationDate
            if (workRole.getCreationDate() != null) {
                stmt.setDate(4, (Date) workRole.getCreationDate());
            } else {
                stmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            }

            stmt.executeUpdate();  // Executar queryn
        }
    }


    // Hämtar fram alla roller
    public List<WorkRole> getAllWorkRoles() throws SQLException {
        List<WorkRole> roles = new ArrayList<>();
        String query = "SELECT * FROM work_role";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                WorkRole role = new WorkRole(
                        rs.getInt("role_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("salary"),
                        rs.getDate("creation_date")
                );
                roles.add(role);
            }
        }
        return roles;
    }

    // Hämtar endast en roll genom id
    public WorkRole getWorkRoleById(int roleId) throws SQLException {
        String query = "SELECT * FROM work_role WHERE role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new WorkRole(
                            rs.getInt("role_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDouble("salary"),
                            rs.getDate("creation_date")
                    );
                }
            }
        }
        return null;
    }

    // Uppdaterar en roll
    public void updateWorkRole(WorkRole workRole) throws SQLException {
        String query = "UPDATE work_role SET title = ?, description = ?, salary = ?, creation_date = ? WHERE role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, workRole.getTitle());
            stmt.setString(2, workRole.getDescription());
            stmt.setDouble(3, workRole.getSalary());
            stmt.setDate(4, (Date) workRole.getCreationDate());
            stmt.setInt(5, workRole.getRoleId());
            stmt.executeUpdate();
        }
    }

    // Tar bort en work role
    public void deleteWorkRole(int roleId) throws SQLException {
        String query = "DELETE FROM work_role WHERE role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            stmt.executeUpdate();
        }
    }
}