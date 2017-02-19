import java.sql.*;

/**
 * Created by sara on 06/02/17.
 */
public class DatabaseManager {

    public Connection connection = null;
    public ResultSet resultSet = null;
    private Statement stmt = null;
    private PreparedStatement prep_stmt = null;

    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:medication.db");
            connection.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Database opened successfully");
    }

    //////////////////////////////////////////////////////////////////////////////////
    //        MEDICATION: FUNCTIONALITY TO GET INFORMATION FROM MEDICATION DB       //
    //////////////////////////////////////////////////////////////////////////////////

    public void addMedication(String pharma_company, String med_name, Double strength, String barcode, int tablets) {
        try {
            prep_stmt = connection.prepareStatement("INSERT INTO medication(pharma_company, med_name, strength, barcode, tablets) VALUES (?,?,?,?,?)");
            prep_stmt.setString(1, pharma_company);
            prep_stmt.setString(2, med_name);
            prep_stmt.setDouble(3, strength);
            prep_stmt.setString(4, barcode);
            prep_stmt.setInt(5, tablets);
            prep_stmt.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Deletes from the user's medication profile
     * @param drugToDelete
     */
    public void delete(String drugToDelete) {
        try {
            prep_stmt = connection.prepareStatement("DELETE FROM medication WHERE med_name=(?)");
            prep_stmt.setString(1, drugToDelete);
            prep_stmt.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Dumps the contents of the database; requires an active connection to the database
     * @return ResultSet of the database
     */
    public ResultSet getMedication() {
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery("SELECT * FROM medication");
            return resultSet;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }
}