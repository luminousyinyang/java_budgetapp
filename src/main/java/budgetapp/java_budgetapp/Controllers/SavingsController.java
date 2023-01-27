package budgetapp.java_budgetapp.Controllers;

import budgetapp.java_budgetapp.HelperClasses.DatabaseConnection;
import budgetapp.java_budgetapp.HelperClasses.utilFuncs;
import budgetapp.java_budgetapp.Main;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class SavingsController implements Initializable {
    @FXML
    private TextField SavingsGoalAmtTextField;
    @FXML
    private Button closeAppButton;
    @FXML
    private JFXHamburger haml;
    @FXML
    private AnchorPane hammenu;


    public void transitionOnAction(ActionEvent event) {
        Button btn = (Button) event.getSource();
        utilFuncs.transitionOnAction(event, btn, btn.getId());
    }

    Connection connection = DatabaseConnection.getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;

    public void savingsGoalSubmitButtonOnAction(ActionEvent event) {
        String SavingsGoalAmtTextFieldText = SavingsGoalAmtTextField.getText();
        if (!SavingsGoalAmtTextFieldText.isEmpty()) {
            try {
                ps = connection.prepareStatement("SELECT count(1) FROM savingsgoal WHERE userid = ?");
                ps.setInt(1, Main.userid);
                rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) != 1) {
                    ps = connection.prepareStatement("INSERT INTO savingsgoal (savedupval, goalval, userid, accountid) VALUES (?, ?, ?, ?)");
                    ps.setDouble(1, 0);
                    ps.setDouble(2, Double.parseDouble(SavingsGoalAmtTextFieldText));
                    ps.setInt(3, Main.userid);
                    ps.setInt(4, Main.accountid);
                    ps.executeUpdate();
                } else {
                    ps = connection.prepareStatement("UPDATE savingsgoal SET goalval = ? WHERE userid = ?");
                    ps.setDouble(1, Double.parseDouble(SavingsGoalAmtTextFieldText));
                    ps.setInt(2, Main.userid);
                    ps.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        utilFuncs.hamburgermenu(haml, hammenu);
    }
}
