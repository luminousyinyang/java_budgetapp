package budgetapp.java_budgetapp.Controllers;

import budgetapp.java_budgetapp.HelperClasses.DatabaseConnection;
import budgetapp.java_budgetapp.HelperClasses.TransactionClass;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.text.DateFormat;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    @FXML
    private JFXHamburger haml;
    @FXML
    private AnchorPane hammenu;
    @FXML
    private ChoiceBox<String> TransCategoryChoiceBox;
    @FXML
    private ChoiceBox TransIDChoiceBox;
    @FXML
    private TextField TransDateTextField;
    @FXML
    private TextField TransAmtTextField;
    @FXML
    private TextField TransNameTextField;
    @FXML
    private TableView<TransactionClass> transactionTableView;
    @FXML
    private TableColumn<TransactionClass, Integer> transIDTable;
    @FXML
    private TableColumn<TransactionClass, String> transNameTable;
    @FXML
    private TableColumn<TransactionClass, String> transCatTable;
    @FXML
    private TableColumn<TransactionClass, String> transDateTable;
    @FXML
    private TableColumn<TransactionClass, Double> transAmtTable;

    public void transitionOnAction(ActionEvent event) {
        Button btn = (Button) event.getSource();
        utilFuncs.transitionOnAction(event, btn, btn.getId());
    }

    Connection connection = DatabaseConnection.getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;

    public void changeTransButtonOnAction(ActionEvent event) {
        String TransCategoryChoiceBoxText = TransCategoryChoiceBox.getValue();
        String TransIDChoiceBoxText = TransIDChoiceBox.getValue().toString();
        String TransDateTextFieldText = TransDateTextField.getText();
        String TransAmtTextFieldText = TransAmtTextField.getText();
        String TransNameTextFieldText = TransNameTextField.getText();
        if (!TransCategoryChoiceBoxText.equals("Existing Categories") && !TransIDChoiceBoxText.equals("Existing Transaction IDs")
        && !TransDateTextFieldText.isEmpty() && !TransAmtTextFieldText.isEmpty() && !TransNameTextFieldText.isEmpty()) {
            try {
                int categoryid = utilFuncs.selectCatByName(TransCategoryChoiceBoxText);
                ps = connection.prepareStatement("UPDATE transaction SET userid = ?, date = ?, accountid = ?," +
                        "categoryid = ?, amount = ?, transactionname = ? WHERE transactionid = ?");
                ps.setInt(1, Main.userid);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                java.util.Date date = formatter.parse(TransDateTextFieldText);
                ps.setDate(2, new Date(date.getTime()));
                ps.setInt(3, Main.accountid);
                ps.setInt(4, categoryid);
                ps.setDouble(5, Double.parseDouble(TransAmtTextFieldText));
                ps.setString(6, TransNameTextFieldText);
                ps.setInt(7,Integer.parseInt(TransIDChoiceBoxText));
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        utilFuncs.hamburgermenu(haml, hammenu);

        utilFuncs.initAllCat(TransCategoryChoiceBox, "Existing Categories");

        ObservableList<TransactionClass> osTable = FXCollections.observableArrayList();
        TransIDChoiceBox.setValue("Existing Transaction IDs");
        try {
            ps = connection.prepareStatement("SELECT * FROM transaction WHERE userid = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            ObservableList<Integer> os = FXCollections.observableArrayList();
            HashMap<Integer, String> categoryNames = utilFuncs.initCatNameIDHash();
            TransactionClass ts = null;
            while (rs.next()) {
                os.add(rs.getInt("transactionid"));

                java.util.Date utilDate = new java.util.Date(rs.getDate("date").getTime());
                DateFormat dateFormat = utilFuncs.formatter;
                final String stringDate = dateFormat.format(utilDate);

                ts = new TransactionClass(rs.getInt("transactionid"), rs.getString("transactionname"),
                        categoryNames.get(rs.getInt("categoryid")), stringDate, rs.getDouble("amount"));
                osTable.add(ts);
            }
            os = os.sorted();

            TransIDChoiceBox.setItems(os);
        } catch (Exception e) {
            e.printStackTrace();
        }


        transIDTable.setCellValueFactory(new PropertyValueFactory<TransactionClass, Integer>("TransID"));
        transNameTable.setCellValueFactory(new PropertyValueFactory<TransactionClass, String>("TransName"));
        transCatTable.setCellValueFactory(new PropertyValueFactory<TransactionClass, String>("TransCategory"));
        transDateTable.setCellValueFactory(new PropertyValueFactory<TransactionClass, String>("TransDate"));
        transAmtTable.setCellValueFactory(new PropertyValueFactory<TransactionClass, Double>("TransAmt"));

        transactionTableView.setItems(osTable);
    }
}
