import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        // JDBC URL for SQL Server Express named instance
        String url =
                "jdbc:sqlserver://localhost:1433;" +
                        "databaseName=TestDB;" +
                        "encrypt=true;" +
                        "trustServerCertificate=true;";

        String user = "testuser";
        String password = "TestUser@12345";


        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connection successful!");

            String sql = "SELECT StudentId, Name, Age, City FROM Students";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("Data from Students table:");
                while (rs.next()) {
                    int id = rs.getInt("StudentId");
                    String name = rs.getString("Name");
                    int age = rs.getInt("Age");
                    String city = rs.getString("City");
                    System.out.printf("%d | %s | %d | %s%n", id, name, age, city);
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection failed:");
            e.printStackTrace();
        }
    }
}
