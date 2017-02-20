import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by sara on 10/02/17.
 */
public class MedicationPrescription extends JDialog {
    private JButton enterDetailsManuallyButton;
    private JLabel displaypiclabel;
    private JPanel medProfilePanel;
    private JTextField manufacturer;
    private JTextField prescriptionName;
    private JFormattedTextField barcode;
    private JFormattedTextField prescriptionStrength;
    private JFormattedTextField numberOfTablets;
    private JButton cancelButton;
    private MaskFormatter barcodeNumber;
    private boolean updated;
    private String[] toSend;

    public MedicationPrescription() {
        $$$setupUI$$$();
        this.updated = false;
        this.toSend = new String[5];
        /* Set formatting of text field */
        try {
            /* Set formatted field for barcode number */
            barcodeNumber = new MaskFormatter("#############");
            barcodeNumber.setPlaceholder("1234567890123");
            DefaultFormatterFactory dff = new DefaultFormatterFactory(barcodeNumber);
            barcode.setFormatterFactory(dff);
            /* Set formatted field for strength */
            NumberFormat f = DecimalFormat.getInstance();
            f.setMaximumFractionDigits(2);
            f.setMinimumFractionDigits(1);
            f.setRoundingMode(RoundingMode.HALF_UP);
            NumberFormatter df = new NumberFormatter(f);
            DefaultFormatterFactory doubleff = new DefaultFormatterFactory(df);
            prescriptionStrength.setFormatterFactory(doubleff);
            prescriptionStrength.setValue(3.14);
            /* Set formatted field for number of tablets */
            NumberFormat d = NumberFormat.getIntegerInstance();
            NumberFormatter intFormatter = new NumberFormatter(d);
            DefaultFormatterFactory intff = new DefaultFormatterFactory(intFormatter);
            numberOfTablets.setFormatterFactory(intff);
            numberOfTablets.setValue(12);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        setContentPane(medProfilePanel);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setVisible(true);

        /* Closes the window without taking action */
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MedicationPrescription.this.dispose();
            }
        });

        /* Submits the data to the database */
        enterDetailsManuallyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getValues();
                if (toSend[1].length() < 13) {
                    JOptionPane.showMessageDialog(MedicationPrescription.this, "Unable to add to profile: \nInvalid barcode value (not 13 digits)");
                    return;
                }
                for (int i = 0; i < 5; i++) {
                    try {
                        /* Checks to see if the values entered are correct */
                        if (toSend[i].equals("") || ((int) Double.parseDouble(toSend[i])) == 0) {
                            JOptionPane.showMessageDialog(MedicationPrescription.this, "Profile not updated: \nPlease check that the values submitted are valid");
                            return;
                        }
                    } catch (Exception parseError) {
                        /* If values cannot be parsed by above, they are valid */
                        continue;
                    }
                }
                updated = true;
                MedicationPrescription.this.dispose();
            }
        });
    }

    /**
     * Updates the value to true when values are successfully submitted to the database
     *
     * @return
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Gets and validates the input
     *
     * @return
     */
    public String[] getValues() {
        toSend[0] = prescriptionName.getText();
        /* Strips all whitespace from mask to ensure the length is 13 digits */
        toSend[1] = barcode.getText().replaceAll("\\s+", "");
        toSend[2] = manufacturer.getText();
        toSend[3] = prescriptionStrength.getText();
        toSend[4] = numberOfTablets.getText();

        return toSend;
    }

    private void createUIComponents() {
        try {
            BufferedImage myPicture = ImageIO.read(this.getClass().getResource("icon/ninja-resized.png"));
            displaypiclabel = new JLabel(new ImageIcon(myPicture));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        medProfilePanel = new JPanel();
        medProfilePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(18, 2, new Insets(30, 30, 30, 30), -1, -1));
        medProfilePanel.setBackground(new Color(-1118482));
        medProfilePanel.setMinimumSize(new Dimension(450, 650));
        medProfilePanel.setPreferredSize(new Dimension(450, 650));
        final JLabel label1 = new JLabel();
        label1.setText("Add a prescription to your profile");
        medProfilePanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        medProfilePanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(11, 10), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        medProfilePanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        displaypiclabel.setText("");
        medProfilePanel.add(displaypiclabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        medProfilePanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        medProfilePanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(14, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        prescriptionName = new JTextField();
        prescriptionName.setHorizontalAlignment(0);
        prescriptionName.setMargin(new Insets(5, 5, 5, 5));
        prescriptionName.setText("Prescription Name");
        medProfilePanel.add(prescriptionName, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        manufacturer = new JTextField();
        manufacturer.setHorizontalAlignment(0);
        manufacturer.setMargin(new Insets(5, 5, 5, 5));
        manufacturer.setText("Manufacturer Name");
        medProfilePanel.add(manufacturer, new com.intellij.uiDesigner.core.GridConstraints(11, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        barcode = new JFormattedTextField();
        barcode.setFocusLostBehavior(0);
        barcode.setHorizontalAlignment(0);
        barcode.setMargin(new Insets(5, 5, 5, 5));
        barcode.setText("13 digits");
        barcode.setToolTipText("Only accepts numeric characters.");
        medProfilePanel.add(barcode, new com.intellij.uiDesigner.core.GridConstraints(10, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        prescriptionStrength = new JFormattedTextField();
        prescriptionStrength.setHorizontalAlignment(0);
        prescriptionStrength.setMargin(new Insets(5, 5, 5, 5));
        prescriptionStrength.setText("");
        medProfilePanel.add(prescriptionStrength, new com.intellij.uiDesigner.core.GridConstraints(12, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        numberOfTablets = new JFormattedTextField();
        numberOfTablets.setHorizontalAlignment(0);
        numberOfTablets.setMargin(new Insets(5, 5, 5, 5));
        numberOfTablets.setText("Number of tablets in packet");
        medProfilePanel.add(numberOfTablets, new com.intellij.uiDesigner.core.GridConstraints(13, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Code Ninja");
        medProfilePanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Name of prescription");
        medProfilePanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        medProfilePanel.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Barcode value");
        medProfilePanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(10, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Name of manufacturer");
        medProfilePanel.add(label5, new com.intellij.uiDesigner.core.GridConstraints(11, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Prescription strength");
        medProfilePanel.add(label6, new com.intellij.uiDesigner.core.GridConstraints(12, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Tablets in packet");
        medProfilePanel.add(label7, new com.intellij.uiDesigner.core.GridConstraints(13, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enterDetailsManuallyButton = new JButton();
        enterDetailsManuallyButton.setMargin(new Insets(5, 14, 5, 14));
        enterDetailsManuallyButton.setText("Submit Details");
        enterDetailsManuallyButton.setToolTipText("Provide all the information on your prescriptions manually.");
        medProfilePanel.add(enterDetailsManuallyButton, new com.intellij.uiDesigner.core.GridConstraints(15, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setEnabled(false);
        label8.setHorizontalAlignment(0);
        label8.setHorizontalTextPosition(0);
        label8.setText("Please provide the following information:");
        medProfilePanel.add(label8, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        medProfilePanel.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("Close this window without making submitting any changes.");
        medProfilePanel.add(cancelButton, new com.intellij.uiDesigner.core.GridConstraints(17, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        medProfilePanel.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(16, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return medProfilePanel;
    }
}
