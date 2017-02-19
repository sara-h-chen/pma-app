import javax.swing.*;
import java.awt.*;

/**
 * Created by sara on 17/02/17.
 */
public class Main {

    public CardLayout card;
    public Dashboard dashboard;

    public Main() {

    }

    private void showGroupLayout() {
        Dashboard dashboard = new Dashboard();
        GroupLayout layout = new GroupLayout(dashboard);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
    }

    public static void main(String[] args) {

    }


}
