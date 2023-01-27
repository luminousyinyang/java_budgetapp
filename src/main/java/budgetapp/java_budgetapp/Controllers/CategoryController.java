package budgetapp.java_budgetapp.Controllers;

import budgetapp.java_budgetapp.HelperClasses.DatabaseConnection;
import budgetapp.java_budgetapp.HelperClasses.utilFuncs;
import budgetapp.java_budgetapp.Main;
import com.jfoenix.controls.JFXHamburger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private ChoiceBox<String> ExistingCatChoiceBox;
    @FXML
    private TextField updatedCatNameTextField;
    @FXML
    private TextField updatedCatBudgetAmtTextField;
    @FXML
    private TextField addCatNameTextField;
    @FXML
    private TextField addCatBudgetAmtTextField;
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

    public void addCatButtonOnAction(ActionEvent event) {
        String addCatNameTextFieldText = addCatNameTextField.getText();
        String addCatBudgetAmtTextFieldText = addCatBudgetAmtTextField.getText();
        if (!addCatNameTextFieldText.isEmpty() && !addCatBudgetAmtTextFieldText.isEmpty()) {
            try {
                ps = connection.prepareStatement("INSERT INTO category (categoryname, userid) VALUES (?, ?)");
                ps.setString(1, addCatNameTextFieldText);
                ps.setInt(2, Main.userid);
                ps.executeUpdate();

                int categoryid = utilFuncs.selectCatByName(addCatNameTextFieldText);

                ps = connection.prepareStatement("INSERT INTO budget (categoryid, amount, date, userid) VALUES (?, ?, ?, ?)");
                ps.setInt(1, categoryid);
                ps.setDouble(2, Double.parseDouble(addCatBudgetAmtTextFieldText));
                java.util.Date date = utilFuncs.formatter.parse(LocalDate.now().toString());
                ps.setDate(3, new java.sql.Date(date.getTime()));
                ps.setInt(4, Main.userid);
                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill in all fields to add a category");
            alert.show();
        }
        utilFuncs.changeScene(event, "changeCat.fxml");
    }

    public void updateCatButtonOnAction(ActionEvent event) {
        String updatedCatNameTextFieldText = updatedCatNameTextField.getText();
        if (!updatedCatNameTextFieldText.isEmpty()) {
            try {
                ps = connection.prepareStatement("UPDATE category SET categoryname = ? WHERE categoryname = ?");
                ps.setString(1, updatedCatNameTextFieldText);
                ps.setString(2, ExistingCatChoiceBox.getValue());
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String updatedCatBudgetAmtTextFieldText = updatedCatBudgetAmtTextField.getText();
        if (!updatedCatBudgetAmtTextFieldText.isEmpty()) {
            try {
                int categoryid = utilFuncs.selectCatByName(updatedCatNameTextFieldText);
                    ps = connection.prepareStatement("UPDATE budget SET amount = ? WHERE categoryid = ?");
                    ps.setDouble(1, Double.parseDouble(updatedCatBudgetAmtTextFieldText));
                    ps.setInt(2, categoryid);
                    ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        utilFuncs.changeScene(event, "changeCat.fxml");
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        utilFuncs.hamburgermenu(haml, hammenu);

        utilFuncs.initAllCat(ExistingCatChoiceBox, "Existing Categories");
    }
}
