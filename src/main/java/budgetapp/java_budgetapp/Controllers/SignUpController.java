package budgetapp.java_budgetapp.Controllers;

import budgetapp.java_budgetapp.HelperClasses.utilFuncs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SignUpController {


    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordPasswordField;
    @FXML
    private Button cancelButton;

    public void backLogInButtonOnAction(ActionEvent event) {
        utilFuncs.changeScene(event, "login.fxml");
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void createSignUpButtonOnAction(ActionEvent event) {
        if (!usernameTextField.getText().trim().isEmpty() && !passwordPasswordField.getText().trim().isEmpty()) {
            utilFuncs.signUpUser(event, firstNameTextField.getText(), lastNameTextField.getText(), usernameTextField.getText(), passwordPasswordField.getText());
        } else {
            System.out.println("Please fill in all information");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill in all information to sign up!");
            alert.show();
        }
    }



}
