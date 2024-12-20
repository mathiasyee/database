import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.mathias.databas.WorkRole;
import se.mathias.databas.WorkRoleDAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkRoleDAOTest {

    private Connection connection;
    private WorkRoleDAO workRoleDAO;

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(fis);
        }
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        connection = DriverManager.getConnection(url, user, password);
        createTableIfNotExists(connection);
        workRoleDAO = new WorkRoleDAO(connection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        clearTable();
        connection.close();
    }

    private void createTableIfNotExists(Connection connection) throws SQLException {
        String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS work_role (
                        role_id INT PRIMARY KEY AUTO_INCREMENT,
                        title VARCHAR(50) NOT NULL,
                        description VARCHAR(50) NOT NULL,
                        salary DOUBLE NOT NULL,
                        creation_date DATE NOT NULL
                    )
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    private void clearTable() throws SQLException {
        String deleteSQL = "DELETE FROM work_role";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteSQL);
        }
    }

    private void printRoles(Connection connection) throws SQLException {
        String query = "SELECT * FROM work_role";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Roles in database:");
            while (rs.next()) {
                System.out.println(
                        "ID: " + rs.getInt("role_id") +
                                ", Title: " + rs.getString("title") +
                                ", Description: " + rs.getString("description") +
                                ", Salary: " + rs.getDouble("salary") +
                                ", Creation Date: " + rs.getDate("creation_date")
                );
            }
        }
    }

    @Test
    public void testInsertAndRetrieveWorkRole() throws SQLException {
        java.sql.Date sqlDate = java.sql.Date.valueOf("2024-12-18");

        // Skapar en work role object för att inserta i databasen
        WorkRole role = new WorkRole(1, "Test Kock", "Testar Mat", 50000, sqlDate);

        // insertar workrole i databasen
        workRoleDAO.insertWorkRole(role);

        // Hämtar alla roller
        List<WorkRole> roles = workRoleDAO.getAllWorkRoles();

        // Checkar assertions
        assertEquals(1, roles.size(), "Det ska finnas exakt en arbetsroll i listan");
        assertEquals("Test Kock", roles.getFirst().getTitle(), "Titel ska vara 'Test Kock'");
        assertEquals("Testar Mat", roles.getFirst().getDescription(), "Beskrivning ska vara 'Testar Mat'");
        assertEquals(50000.0, roles.getFirst().getSalary(), "Lön ska vara 50000");

        printRoles(connection);
    }
}
