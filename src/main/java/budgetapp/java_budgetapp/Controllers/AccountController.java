package budgetapp.java_budgetapp.Controllers;

import budgetapp.java_budgetapp.HelperClasses.DatabaseConnection;
import budgetapp.java_budgetapp.HelperClasses.utilFuncs;
import budgetapp.java_budgetapp.Main;
import com.jfoenix.controls.JFXHamburger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    @FXML
    private Label accFirstNameTextField;
    @FXML
    private Label accLastNameTextField;
    @FXML
    private Label accUsernameTextField;
    @FXML
    private ChoiceBox<String> accSetChoiceBox;
    @FXML
    private TextField updatedAccValTextField;
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

    public void updateAccSetButtonOnAction(ActionEvent event) {
        String accSetChoiceBoxText = accSetChoiceBox.getValue();
        String updatedAccValTextFieldText = updatedAccValTextField.getText();
        if (!accSetChoiceBoxText.equals("Account Properties")) {
            try {
                ps = connection.prepareStatement("UPDATE useraccount SET " + accSetChoiceBoxText + " = ? WHERE iduseraccount = ?");
                ps.setString(1, updatedAccValTextFieldText);
                ps.setInt(2, Main.userid);
                ps.executeUpdate();

                if (accSetChoiceBoxText.equals("firstname")) {
                    ps = connection.prepareStatement("UPDATE account SET accountname = ? WHERE userid = ?");
                    ps.setString(1, updatedAccValTextFieldText);
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


        ObservableList<String> observableList = FXCollections.observableArrayList("firstname", "lastname", "username", "password");
        accSetChoiceBox.setValue("Account Properties");
        accSetChoiceBox.setItems(observableList);

        try {
            ps = connection.prepareStatement("SELECT * FROM useraccount WHERE iduseraccount = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            rs.next();

            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String username = rs.getString("username");

            accFirstNameTextField.setText("First Name: " + firstName);
            accLastNameTextField.setText("Last Name: " + lastName);
            accUsernameTextField.setText("Username: " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
