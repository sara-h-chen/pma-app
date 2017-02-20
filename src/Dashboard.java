import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Vector;

/**
 * Created by sara on 11/02/17.
 */
public class Dashboard extends JFrame {

    private JPanel dashboard;
    private JLabel displayPicLabel;
    private JTable table1;
    private DefaultTableModel tableModel;
    private JButton updatePrescriptionsButton;
    private JButton checkCompatibilityButton;
    private JButton checkOverTheCounterButton;
    private JButton updateButton;
    private JButton shortcutsButton;
    private JPanel shortcutsPanel;
    private JSplitPane dashboardPane;
    private DatabaseManager dbManager;

    public Dashboard() {
        super("Dashboard");
        this.dbManager = new DatabaseManager();
        $$$setupUI$$$();
        /* ADD ICONS TO BUTTONS */
        updatePrescriptionsButton.setIcon(new ImageIcon(Dashboard.class.getResource("icon/medicine-stethoscope-icon.png")));
        checkCompatibilityButton.setIcon(new ImageIcon(Dashboard.class.getResource("icon/life_star-512.png")));
        shortcutsButton.setIcon(new ImageIcon(Dashboard.class.getResource("icon/hamburger.png")));
        /* REMOVE BORDERS AROUND BUTTONS */
        updatePrescriptionsButton.setBorderPainted(false);
        checkCompatibilityButton.setBorderPainted(false);
        shortcutsButton.setBorderPainted(false);
        /* CHANGE CURSOR ON HOVER FOR BUTTONS */
        updatePrescriptionsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkCompatibilityButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        shortcutsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        setContentPane(dashboard);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
        shortcutsPanel.setVisible(false);

        /**
         * Shows and hides the side nav bar/menu
         */
        shortcutsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!shortcutsPanel.isVisible()) {
                    dashboardPane.setDividerLocation(0.28);
                    shortcutsPanel.setVisible(true);
                } else {
                    shortcutsPanel.setVisible(false);
                }
            }
        });

        checkCompatibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(Dashboard.this, "You keep clicking");
            }
        });

        /**
         * The next two functions update the user's medication profile
         */
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToDatabase();
            }
        });

        updatePrescriptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToDatabase();
            }
        });

    }

    private void addToDatabase() {
        final MedicationPrescription medForm = new MedicationPrescription();
        medForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                String[] values = medForm.getValues();
                if (values != null && medForm.isUpdated()) {
                    dbManager.addMedication(values[2], values[0], Double.parseDouble(values[3]), values[1], Integer.parseInt(values[4]));
                    tableModel.addRow(new Object[]{values[2], values[0], values[3], values[1], values[4]});
                    JOptionPane.showMessageDialog(Dashboard.this, "Prescription added to profile", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    return;
                }
            }
        });
    }

    private void createUIComponents() {
        try {
            BufferedImage myPicture = ImageIO.read(this.getClass().getResource("icon/ninja-resized.png"));
            displayPicLabel = new JLabel(new ImageIcon(myPicture));


            tableModel = buildTableModel();
            table1 = new JTable(tableModel) {
                public boolean editCellAt(int row, int column, EventObject e) {
                    return false;
                }
            };
            table1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            /* Allow deletion upon double click */
            table1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();
                        int dialogResult = JOptionPane.showConfirmDialog(Dashboard.this, "Are you sure you want to delete this?");
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            String medToDelete = target.getValueAt(row, 1).toString();
                            dbManager.delete(medToDelete);
                            tableModel.removeRow(row);
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DefaultTableModel buildTableModel() {
        try {
            ResultSet rs = this.dbManager.getMedication();
            ResultSetMetaData metaData = rs.getMetaData();
            String col[] = {"Manufacturer", "Prescription Name", "Strength (mg)", "Barcode", "Tablets/Pack"};

            /* Gets names of columns */
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            for (int column = 0; column < columnCount; column++) {
                columnNames.add(col[column]);
            }

            /* Gets data for the table */
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            return new DefaultTableModel(data, columnNames);

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }


    public static void main(String[] args) {
        Dashboard dashboard = new Dashboard();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        dashboard = new JPanel();
        dashboard.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(20, 20, 20, 20), -1, -1));
        dashboardPane = new JSplitPane();
        dashboard.add(dashboardPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-1118482));
        dashboardPane.setRightComponent(panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-1118482));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(557, 89), null, 0, false));
        displayPicLabel.setText("");
        panel2.add(displayPicLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setFont(new Font("Impact", label1.getFont().getStyle(), 36));
        label1.setForeground(new Color(-3597799));
        label1.setText("DASHBOARD");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        shortcutsButton = new JButton();
        shortcutsButton.setBackground(new Color(-1118482));
        shortcutsButton.setMargin(new Insets(2, 2, 2, 14));
        shortcutsButton.setText("Shortcuts");
        panel2.add(shortcutsButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(0);
        label2.setText("Code Ninja's Medication Profile");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(557, 18), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(557, 14), null, 0, false));
        checkOverTheCounterButton = new JButton();
        checkOverTheCounterButton.setText("Check Over-The-Counter Medication for Possible Interactions");
        checkOverTheCounterButton.setToolTipText("Check for any possible Drug-Drug Interactions between your prescription drugs and Over-The-Counter medication.");
        panel1.add(checkOverTheCounterButton, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(557, 34), null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Update Medical Profile");
        updateButton.setToolTipText("Make changes to your medical profile.");
        panel1.add(updateButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(557, 34), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(557, 14), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBackground(new Color(-1118482));
        scrollPane1.setToolTipText("Your current prescriptions. Double-click on table entry to delete from profile.");
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(600, 200), null, 1, false));
        table1.setAutoCreateRowSorter(true);
        table1.setIntercellSpacing(new Dimension(5, 5));
        table1.setRowHeight(20);
        scrollPane1.setViewportView(table1);
        shortcutsPanel = new JPanel();
        shortcutsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        shortcutsPanel.setBackground(new Color(-562358));
        dashboardPane.setLeftComponent(shortcutsPanel);
        updatePrescriptionsButton = new JButton();
        updatePrescriptionsButton.setBackground(new Color(-9567737));
        updatePrescriptionsButton.setContentAreaFilled(false);
        updatePrescriptionsButton.setForeground(new Color(-1));
        updatePrescriptionsButton.setText("Update Prescriptions");
        updatePrescriptionsButton.setToolTipText("Keep your profile up-to-date. Make changes to your current prescriptions by clicking here.");
        shortcutsPanel.add(updatePrescriptionsButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 34), null, 0, false));
        checkCompatibilityButton = new JButton();
        checkCompatibilityButton.setBackground(new Color(-2220773));
        checkCompatibilityButton.setContentAreaFilled(false);
        checkCompatibilityButton.setForeground(new Color(-1));
        checkCompatibilityButton.setText("Check Compatibility");
        checkCompatibilityButton.setToolTipText("Check for any possible Drug-Drug Interactions between your prescription drugs and Over-The-Counter medication by entering its details here.");
        shortcutsPanel.add(checkCompatibilityButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        shortcutsPanel.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setFont(new Font("Purisa", label3.getFont().getStyle(), 18));
        label3.setForeground(new Color(-3597799));
        label3.setText("Shortcuts");
        shortcutsPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return dashboard;
    }
}
