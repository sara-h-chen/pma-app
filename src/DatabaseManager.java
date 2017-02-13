import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sara on 06/02/17.
 */
public class DatabaseManager {

    public Connection connection = null;
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
     * Edit Profile Name
     * @param current_user_name
     * @param new_user_name
     */
    public void changeName(String current_user_name, String new_user_name) {
        try {
            openDatabase();

            /* UPDATE USERNAME IN DB */
            prep_stmt = connection.prepareStatement("UPDATE user_profile SET name=(?) WHERE name=(?)");
            prep_stmt.setString(1, new_user_name);
            prep_stmt.setString(2, current_user_name);
            prep_stmt.executeUpdate();
            connection.commit();

            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Updates the user's medical profile
     * Links user profile to medication in database
     * @param user_name
     */
    public void updateProfile(String user_name, String med_name, String barcode, int quantity) {
        try {
            openDatabase();

            /* GET THE AUTO-ASSIGNED USER ID FROM USER DATABASE */
            prep_stmt = connection.prepareStatement("SELECT id FROM user_profile WHERE name=(?)");
            prep_stmt.setString(1, user_name);
            resultSet = prep_stmt.executeQuery();
            int user_id = resultSet.getInt("id");

            /* GET THE MEDICATION ID */
            prep_stmt = connection.prepareStatement("SELECT id FROM medication WHERE med_name=(?) OR barcode=(?)");
            prep_stmt.setString(1, med_name);
            prep_stmt.setString(2, barcode);
            resultSet = prep_stmt.executeQuery();
            int med_id = resultSet.getInt("id");

            /* LINK USER TO MEDICATION IN MED DB */
            prep_stmt = connection.prepareStatement("INSERT INTO user_medication(user_id, medication_id, tablet_quantity) VALUES (?,?,?)");
            prep_stmt.setInt(1, user_id);
            prep_stmt.setInt(2, med_id);
            prep_stmt.setInt(3, quantity);
            prep_stmt.executeUpdate();
            connection.commit();

            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public Medicine returnMedObject(String user_name) {
        try {
            openDatabase();


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    /**
     * Gets all the user's prescribed medication IDs
     * @param user_name
     * @return
     */
    public ArrayList<Integer> returnMedicationId(String user_name) {
        try {
            openDatabase();

            /* GET ALL MEDICATION */
            prep_stmt = connection.prepareStatement("SELECT * FROM user_medication LEFT JOIN user_profile AS userdb ON user_medication.user_id=userdb.id WHERE userdb.name=(?)");
            prep_stmt.setString(1, user_name);
            resultSet = prep_stmt.executeQuery();
            ArrayList<Integer> medicationList = new ArrayList<Integer>();
            while (resultSet.next()) {
                int medication = resultSet.getInt("medication_id");
                medicationList.add(medication);
            }

            connection.close();

            return medicationList;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    /**
     * Returns all information on the medication that the user is taking
     * @param user_name
     * @return
     */
    public ArrayList<Map<String, String>> userMedication(String user_name) {
        try {
            openDatabase();

            prep_stmt = connection.prepareStatement("SELECT id FROM user_profile WHERE name=(?)");
            prep_stmt.setString(1, user_name);
            resultSet = prep_stmt.executeQuery();

            /* USE USER ID TO GET MEDICATION */
            prep_stmt = connection.prepareStatement("SELECT * FROM medication LEFT JOIN user_medication AS um ON medication.id=um.medication_id WHERE um.user_id=(?)");
            prep_stmt.setString(1, Integer.toString(resultSet.getInt("id")));
            resultSet = prep_stmt.executeQuery();

            ArrayList<Map<String, String>> arrayOfMeds = new ArrayList<Map<String, String>>();
            while (resultSet.next()) {
                HashMap<String, String> hash = new HashMap<String, String>();
                String med_id = Integer.toString(resultSet.getInt("id"));
                hash.put("id", med_id);
                String pharma_company = resultSet.getString("pharma_company");
                hash.put("pharma_company", pharma_company);
                String med_name = resultSet.getString("med_name");
                hash.put("med_name", med_name);
                String dosage = Integer.toString(resultSet.getInt("strength"));
                hash.put("dosage", dosage);
                String barcode = resultSet.getString("barcode");
                hash.put("barcode", barcode);
                String quantity = Integer.toString(resultSet.getInt("tablet_quantity"));
                hash.put("quantity", quantity);

                arrayOfMeds.add(hash);
            }
            connection.close();
            return arrayOfMeds;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    /**
     * Deletes from the user's medication profile
     * @param rs
     * @param row
     */
    public void delete(ArrayList<Map<String, String>> rs, int row) {
        Map<String, String> selectedRow = rs.get(row);
        try {
            openDatabase();

            prep_stmt = connection.prepareStatement("DELETE FROM user_medication WHERE medication_id=(?)");
            prep_stmt.setInt(1, Integer.valueOf(selectedRow.get("id")));
            prep_stmt.executeUpdate();
            connection.commit();

            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /*
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
     */

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
//        dbManager.userMedication("John Doe");
//        MedicationPrescription myForm = new MedicationPrescription();
//        Dashboard myForm2 = new Dashboard(dbManager.userMedication("John Doe"));
//        OverCounterForm medForm = new OverCounterForm();
        Warning warning = new Warning();
        Safe safe = new Safe();
        Danger danger = new Danger();
    }
}