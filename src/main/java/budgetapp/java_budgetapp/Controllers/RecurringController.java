package budgetapp.java_budgetapp.Controllers;

import budgetapp.java_budgetapp.HelperClasses.DatabaseConnection;
import budgetapp.java_budgetapp.HelperClasses.RecurringClass;
import budgetapp.java_budgetapp.HelperClasses.utilFuncs;
import budgetapp.java_budgetapp.Main;
import com.jfoenix.controls.JFXHamburger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

public class RecurringController implements Initializable {
    @FXML
    private TextField addRecurrNameTextField;
    @FXML
    private ChoiceBox<String> addRecurrCateChoiceBox;
    @FXML
    private TextField addRecurrDateTextField;
    @FXML
    private TextField addRecurrAmtTextField;
    @FXML
    private TextField addRecurrMorYTextField;
    @FXML
    private ChoiceBox ExistingRecurrTransChoiceBox;
    @FXML
    private TableView<RecurringClass> transactionTableView;
    @FXML
    private TableColumn<RecurringClass, Integer> RecurrIDTable;
    @FXML
    private TableColumn<RecurringClass, String> RecurrStartDateTable;
    @FXML
    private TableColumn<RecurringClass, Double> RecurrAmountTable;
    @FXML
    private TableColumn<RecurringClass, String> RecurrMorYTable;
    @FXML
    private TableColumn<RecurringClass, String> RecurrNameTable;
    @FXML
    private TableColumn<RecurringClass, String> RecurrCatTable;
    @FXML
    private TextField changeRecurrNameTextField;
    @FXML
    private ChoiceBox<String> changeCateChoiceBox;
    @FXML
    private TextField changeDateTextField;
    @FXML
    private TextField changeAmtTextField;
    @FXML
    private TextField changeMorYTextField;
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
    public void addRecurrButtonOnAction(ActionEvent event) {
        String addRecurrNameTextFieldText = addRecurrNameTextField.getText();
        String addRecurrCateChoiceBoxText = addRecurrCateChoiceBox.getValue().toString();
        String addRecurrDateTextFieldText = addRecurrDateTextField.getText();
        String addRecurrAmtTextFieldText = addRecurrAmtTextField.getText();
        String addRecurrMorYTextFieldText = addRecurrMorYTextField.getText();
        if (!addRecurrNameTextFieldText.isEmpty() &&
                !addRecurrCateChoiceBoxText.equals("Purchase Category")
                && !addRecurrDateTextFieldText.isEmpty()
                && !addRecurrAmtTextFieldText.isEmpty()
                && !addRecurrMorYTextFieldText.isEmpty()) {
            try {
                int categoryid = utilFuncs.selectCatByName(addRecurrCateChoiceBoxText);

                rs = utilFuncs.checkRecurrExist(addRecurrNameTextFieldText);

                if (rs.getInt(1) == 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Recurring Expense Name already exists, choose a different name.");
                    alert.show();
                    return;
                }
                ps = connection.prepareStatement(
                        "INSERT INTO recurringexpense (categoryid, startdate, enddate, amount," +
                                " permonth, peryear, userid, accountid, active, transactionname) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, categoryid);
                SimpleDateFormat formatter = utilFuncs.formatter;
                java.util.Date date = formatter.parse(addRecurrDateTextFieldText);
                ps.setDate(2, new Date(date.getTime()));
                ps.setDate(3,null);
                ps.setDouble(4,Double.parseDouble(addRecurrAmtTextFieldText));
                if (addRecurrMorYTextFieldText.equals("M")) {
                    ps.setBoolean(5,true);
                    ps.setBoolean(6,false);
                } else if (addRecurrMorYTextFieldText.equals("Y")) {
                    ps.setBoolean(5,false);
                    ps.setBoolean(6,true);
                }
                ps.setInt(7, Main.userid);
                ps.setInt(8, Main.accountid);
                ps.setBoolean(9,true);
                ps.setString(10,addRecurrNameTextFieldText);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

public void changeRecurrButtonOnAction(ActionEvent event) {
        String ExistingRecurrTransChoiceBoxText = ExistingRecurrTransChoiceBox.getValue().toString();
        String changeRecurrNameTextFieldText = changeRecurrNameTextField.getText();
        String changeCateChoiceBoxText = changeCateChoiceBox.getValue();
        String changeDateTextFieldText = changeDateTextField.getText();
        String changeAmtTextFieldText = changeAmtTextField.getText();
        String changeMorYTextFieldText = changeMorYTextField.getText();
    if (!ExistingRecurrTransChoiceBoxText.equals("Existing Recurrings") &&
    !changeRecurrNameTextFieldText.isEmpty() && !changeCateChoiceBoxText.equals("Purchase Category")
    && !changeDateTextFieldText.isEmpty() && !changeAmtTextFieldText.isEmpty() && !changeMorYTextFieldText.isEmpty()) {
        try {
            int categoryid = utilFuncs.selectCatByName(changeCateChoiceBoxText);
            rs = utilFuncs.checkRecurrExist(changeRecurrNameTextFieldText);

            if (rs.getInt(1) == 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Recurring Expense Name already exists, choose a different name.");
                alert.show();
                return;
            }
            ps = connection.prepareStatement("UPDATE recurringexpense SET categoryid = ?, startdate = ?, enddate = ?, amount = ?," +
                    "permonth = ?, peryear = ?, userid = ?, accountid = ?, active = ?, transactionname = ? WHERE recurid = ?");
            ps.setInt(1, categoryid);
            SimpleDateFormat formatter = utilFuncs.formatter;
            java.util.Date date = formatter.parse(changeDateTextFieldText);
            ps.setDate(2, new Date(date.getTime()));
            ps.setDate(3,null);
            ps.setDouble(4,Double.parseDouble(changeAmtTextFieldText));
            if (changeMorYTextFieldText.equals("M")) {
                ps.setBoolean(5,true);
                ps.setBoolean(6,false);
            } else if (changeMorYTextFieldText.equals("Y")) {
                ps.setBoolean(5,false);
                ps.setBoolean(6,true);
            }
            ps.setInt(7,Main.userid);
            ps.setInt(8,Main.accountid);
            ps.setBoolean(9,true);
            ps.setString(10,changeRecurrNameTextFieldText);
            ps.setInt(11, Integer.parseInt(ExistingRecurrTransChoiceBoxText.toString()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        utilFuncs.hamburgermenu(haml, hammenu);


        utilFuncs.initAllCat(addRecurrCateChoiceBox, "Purchase Category");
        utilFuncs.initAllCat(changeCateChoiceBox, "Purchase Category");

        ObservableList<RecurringClass> osTable = FXCollections.observableArrayList();
        ExistingRecurrTransChoiceBox.setValue("Existing Recurrings");
        try {
            ps = connection.prepareStatement("SELECT * FROM recurringexpense WHERE userid = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            ObservableList<Integer> os = FXCollections.observableArrayList();
            HashMap<Integer, String> categoryNames = utilFuncs.initCatNameIDHash();
            RecurringClass rt = null;
            while (rs.next()) {
                os.add(Integer.parseInt(rs.getString("recurid")));

                java.util.Date utilDate = new java.util.Date(rs.getDate("startdate").getTime());
                DateFormat dateFormat = utilFuncs.formatter;
                final String stringDate = dateFormat.format(utilDate);
                String recurrSchedule = null;
                if (rs.getBoolean("permonth")) {
                    recurrSchedule = "Monthly";
                } else {
                    recurrSchedule = "Yearly";
                }

                rt = new RecurringClass(rs.getInt("recurid"), rs.getString("transactionname"),
                        categoryNames.get(rs.getInt("categoryid")), stringDate, recurrSchedule, rs.getDouble("amount"));
                osTable.add(rt);
            }
            os = os.sorted();
            ExistingRecurrTransChoiceBox.setItems(os);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecurrIDTable.setCellValueFactory(new PropertyValueFactory<RecurringClass, Integer>("RecurrID"));
        RecurrNameTable.setCellValueFactory(new PropertyValueFactory<RecurringClass, String>("RecurrName"));
        RecurrCatTable.setCellValueFactory(new PropertyValueFactory<RecurringClass, String>("RecurrCat"));
        RecurrStartDateTable.setCellValueFactory(new PropertyValueFactory<RecurringClass, String>("RecurrStartDate"));
        RecurrMorYTable.setCellValueFactory(new PropertyValueFactory<RecurringClass, String>("RecurrMorY"));
        RecurrAmountTable.setCellValueFactory(new PropertyValueFactory<RecurringClass, Double>("RecurrAmount"));

        transactionTableView.setItems(osTable);
    }
}
