package budgetapp.java_budgetapp.HelperClasses;

import budgetapp.java_budgetapp.Main;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class utilFuncs {

    static Connection connection = DatabaseConnection.getConnection();
    static PreparedStatement ps = null;
    static ResultSet rs = null;
    static public SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static void changeScene(ActionEvent event, String fxmlFile){
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (fxmlFile.equals("login.fxml") || fxmlFile.equals("signup.fxml")) {
            stage.setScene(new Scene(root, 600, 400));
        } else {
            stage.setScene(new Scene(root, 900, 600));
        }
        stage.show();
    }

    public static void closeAppButton(Button closeAppButton) {
        Stage stage = (Stage) closeAppButton.getScene().getWindow();
        stage.close();
    }

    public static void transitionOnAction(ActionEvent event, Button btn, String btnID) {
        switch (btnID) {
            case "menuHomePageButton":
                changeScene(event, "loggedIn.fxml");
                break;
//                WASNT SET ON ALL PAGES
            case "changeCatSet":
                changeScene(event, "changeCat.fxml");
                break;
            case "navChangeAccButton":
                changeScene(event, "changeAccSet.fxml");
                break;
            case "navRecurrTransButton":
                changeScene(event, "recurringTrans.fxml");
                break;
            case "navEditTransButtonOn":
                changeScene(event, "changeTrans.fxml");
                break;
            case "navSavingsButton":
                changeScene(event, "savingsGoal.fxml");
                break;
            case "closeAppButton":
                closeAppButton(btn);
                break;
        }
    }


    public static void hamburgermenu(JFXHamburger haml, AnchorPane hammenu) {
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(haml);
        transition.setRate(-1);
        haml.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate()*-1);
            transition.play();
            if (hammenu.isVisible()) {
                hammenu.setVisible(false);
            } else {
                hammenu.setVisible(true);
            }
        });
    }

    public static int selectCatByName(String catName) {
        int categoryid = -1;
        try {
            ps = connection.prepareStatement("SELECT categoryid FROM category WHERE categoryname = ? AND userid = ?");
            ps.setString(1, catName);
            ps.setInt(2, Main.userid);
            rs = ps.executeQuery();
            rs.next();
            categoryid = rs.getInt("categoryid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryid;
    }
    public static void initAllCat(ChoiceBox<String> cb, String startVal) {
        cb.setValue(startVal);
        try {
            ps = connection.prepareStatement("SELECT categoryname FROM category WHERE userid = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            ObservableList<String> os = FXCollections.observableArrayList();
            while (rs.next()) {
                os.add(rs.getString("categoryname"));
            }
            cb.setItems(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HashMap<Integer, String> initCatNameIDHash() {
        HashMap<Integer, String> categoryNames = new HashMap<Integer, String>();
        try {
            ps = connection.prepareStatement("SELECT categoryid, categoryname FROM category WHERE userid = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    categoryNames.put(rs.getInt("categoryid"), rs.getString("categoryname"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryNames;
    }

    public static ResultSet checkRecurrExist(String recurrName) {
        try {
            ps = connection.prepareStatement("SELECT count(1) FROM recurringexpense WHERE transactionname = ?");
            ps.setString(1, recurrName);
            rs = ps.executeQuery();
            rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void signUpUser(ActionEvent event, String firstName, String lastName, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            psCheckUserExists = connection.prepareStatement("SELECT * FROM useraccount WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO useraccount (firstname, lastname, username, password) VALUES (?, ?, ?, ?)");
                psInsert.setString(1, firstName);
                psInsert.setString(2, lastName);
                psInsert.setString(3, username);
                psInsert.setString(4, password);
                psInsert.executeUpdate();
                psCheckUserExists = connection.prepareStatement("SELECT * FROM useraccount WHERE username = ?");
                psCheckUserExists.setString(1, username);
                resultSet = psCheckUserExists.executeQuery();
                resultSet.next();
                Main.userid = resultSet.getInt("iduseraccount");
                Main.username = username;
                psInsert = connection.prepareStatement("INSERT INTO account (accountname, userid, active) VALUES (?, ?, ?)");
                psInsert.setString(1, firstName);
                psInsert.setInt(2, Main.userid);
                psInsert.setBoolean(3, false);
                psInsert.executeUpdate();
                ps = connection.prepareStatement("SELECT accountid FROM account WHERE userid = ?");
                ps.setInt(1, Main.userid);
                resultSet = ps.executeQuery();
                resultSet.next();
                Main.accountid = resultSet.getInt("accountid");


                utilFuncs.changeScene(event, "loggedIn.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }}


    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement psUpdateRow = null;
        ResultSet resultSet = null;


        try {
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM useraccount WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");

                    if (retrievedPassword.equals(password)) {
                        String retrievedFirstName = resultSet.getString("firstname");
                        String retrievedLastName = resultSet.getString("lastname");
                        Main.userid = resultSet.getInt("iduseraccount");
                        Main.username = resultSet.getString("username");
                        preparedStatement = connection.prepareStatement("SELECT accountid FROM account WHERE userid = ?");
                        preparedStatement.setInt(1, Main.userid);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        Main.accountid = resultSet.getInt("accountid");

                        utilFuncs.changeScene(event, "loggedIn.fxml");
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        alert.show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
