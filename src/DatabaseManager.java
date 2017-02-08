import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Created by sara on 06/02/17.
 */
public class DatabaseManager {

    Connection connection = null;
    ResultSet resultSet = null;
    Statement stmt = null;
    PreparedStatement prep_stmt = null;

    /**
     *  Opens database connection
     */
    public void openDatabase() {
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

    /**
     * Runs a test query to check connection
     */
    public void testQuery() {
        try {
            resultSet = stmt.executeQuery("SELECT * FROM medication;");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String pharmaCompany = resultSet.getString("pharma_company");
                String medName = resultSet.getString("med_name");
                System.out.println("ID = " + id);
                System.out.println("pharmaCompany = " + pharmaCompany);
                System.out.println("medName = " + medName);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Updates the user profile
     * Links user profile to medication in database
     * @param user_name
     */
    public void updateProfile(String user_name, String med_name, String barcode) {
        try {
            this.openDatabase();
            /* INSERT USERNAME INTO USER DATABASE */
            prep_stmt = connection.prepareStatement("INSERT OR IGNORE INTO user(name) VALUES (?)");
            prep_stmt.setString(1, user_name);
            prep_stmt.executeUpdate();
            connection.commit();

            /* GET THE AUTO-ASSIGNED USER ID FROM USER DATABASE */
            prep_stmt = connection.prepareStatement("SELECT id FROM user WHERE name=(?)");
            prep_stmt.setString(1, user_name);
            resultSet = prep_stmt.executeQuery();
            int user_id = resultSet.getInt("id");

            /* */
            prep_stmt = connection.prepareStatement("SELECT id FROM medication WHERE med_name=(?) OR barcode=(?)");
            prep_stmt.setString(1, med_name);
            prep_stmt.setString(2, barcode);
            resultSet = prep_stmt.executeQuery();
            int med_id = resultSet.getInt("id");

            /* LINK USER TO MEDICATION IN MED DB */
            prep_stmt = connection.prepareStatement("INSERT INTO user_medication(user_id, medication_id) VALUES (?,?)");
            prep_stmt.setInt(1, user_id);
            prep_stmt.setInt(2, med_id);
            prep_stmt.executeUpdate();
            connection.commit();

            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Edit Profile Name
     * @param current_user_name
     * @param new_user_name
     */
    public void changeName(String current_user_name, String new_user_name) {
        try {
            this.openDatabase();
            /* UPDATE USERNAME IN DB */
            prep_stmt = connection.prepareStatement("UPDATE user SET name=(?) WHERE name=(?)");
            prep_stmt.setString(1, new_user_name);
            prep_stmt.setString(2, current_user_name);

            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String args[]) {
        DatabaseManager DBManager = new DatabaseManager();
    }
}
