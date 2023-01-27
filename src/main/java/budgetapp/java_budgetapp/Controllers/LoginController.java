package budgetapp.java_budgetapp.Controllers;

import budgetapp.java_budgetapp.HelperClasses.utilFuncs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class LoginController {
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    public void loginButtonOnAction(ActionEvent e) {
        if (usernameTextField.getText().isBlank() == false && passwordPasswordField.getText().isBlank() == false) {
            loginMessageLabel.setText("");
            utilFuncs.logInUser(e, usernameTextField.getText(), passwordPasswordField.getText());
        } else {
            loginMessageLabel.setText("Please enter username and password.");
        }

    }

    public void signUpButtonOnAction(ActionEvent e) {
        utilFuncs.changeScene(e, "signup.fxml");
    }

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}