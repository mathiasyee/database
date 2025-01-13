package se.mathias.databas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/newDB";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "bajskorv";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            WorkRoleDAO dao = new WorkRoleDAO(connection);
            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

            // Insertar en work role
            WorkRole newRole = new WorkRole(0, "Mekaniker", "Lagar bilar", 40000, sqlDate);
            dao.insertWorkRole(newRole);
            System.out.println("Inserted work role ");

            // Hämtar alla work role
            List<WorkRole> roles = dao.getAllWorkRoles();
            System.out.println("All roles:");
            roles.forEach(role -> System.out.println(role.getTitle()));

            //Hämtar en role genom id
            WorkRole role = dao.getWorkRoleById(81);
            if (role != null) {
                System.out.println("Fetched role: " + role.getTitle());

                // Uppdaterar en rolls lön
                System.out.println("Före uppdatering: " + role.getSalary());
                role.setSalary(45000);
                dao.updateWorkRole(role);
                System.out.println("Efter uppdatering: " + role.getSalary());
            }

             //Tar bort en work role
            if (role != null) {
                dao.deleteWorkRole(role.getRoleId());
                System.out.println("Deleted role: " + role.getTitle());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
