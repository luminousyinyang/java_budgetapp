package budgetapp.java_budgetapp.Controllers;

import budgetapp.java_budgetapp.HelperClasses.DatabaseConnection;
import budgetapp.java_budgetapp.HelperClasses.utilFuncs;
import budgetapp.java_budgetapp.Main;
import com.jfoenix.controls.JFXHamburger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoggedInController implements Initializable{
    @FXML
    private JFXHamburger haml;
    @FXML
    private ChoiceBox<String> addTransCatChoiceBox;
    @FXML
    private PieChart pieChart;
    @FXML
    private AnchorPane hammenu;
    @FXML
    private TextField addTransNameTextField;
    @FXML
    private TextField addTransAmtTextField;
    @FXML
    private TextField addTransDateTextField;
    @FXML
    private Label loggedInSavingsLabel;
    @FXML
    private Label loggedInCat1;
    @FXML
    private Label loggedInCat2;
    @FXML
    private Label loggedInCat3;
    @FXML
    private Label loggedInCat4;
    @FXML
    private Label loggedInCat5;
    @FXML
    private Label loggedInCat6;
    @FXML
    private Label loggedInCat1PercentLabel;
    @FXML
    private Label loggedInCat2PercentLabel;
    @FXML
    private Label loggedInCat3PercentLabel;
    @FXML
    private Label loggedInCat4PercentLabel;
    @FXML
    private Label loggedInCat5PercentLabel;
    @FXML
    private Label loggedInCat6PercentLabel;
    @FXML
    private Label totalBudgetUsed;

    public void transitionOnAction(ActionEvent event) {
        Button btn = (Button) event.getSource();
        utilFuncs.transitionOnAction(event, btn, btn.getId());
    }

    Connection connection = DatabaseConnection.getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;

    public void addTransButtonOnAction(ActionEvent event) {
        String addTransCatChoiceBoxText = addTransCatChoiceBox.getValue();
        String addTransNameTextFieldText = addTransNameTextField.getText();
        String addTransAmtTextFieldText = addTransAmtTextField.getText();
        String addTransDateTextFieldText = addTransDateTextField.getText();
        if (!addTransCatChoiceBoxText.isEmpty() && !addTransNameTextFieldText.isEmpty() && !addTransAmtTextFieldText.isEmpty() && !addTransDateTextFieldText.isEmpty()) {
            try {
                int categoryid = utilFuncs.selectCatByName(addTransCatChoiceBoxText);

                ps = connection.prepareStatement("INSERT INTO transaction (userid, date, accountid, categoryid, amount, transactionname) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, Main.userid);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                java.util.Date date = formatter.parse(addTransDateTextFieldText);
                ps.setDate(2, new Date(date.getTime()));
                ps.setInt(3, Main.accountid);
                ps.setInt(4, categoryid);
                ps.setDouble(5, Double.parseDouble(addTransAmtTextFieldText));
                ps.setString(6, addTransNameTextFieldText);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("couldn't add transaction");
            alert.show();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        utilFuncs.hamburgermenu(haml, hammenu);

        try {
            ps = connection.prepareStatement("SELECT goalval FROM savingsgoal WHERE userid = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            while (rs.isBeforeFirst()) {
                rs.next();
                int savingsGoal = rs.getInt("goalval");
                loggedInSavingsLabel.setText("$" + String.valueOf(savingsGoal));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Label[] catArray = new Label[]{loggedInCat1, loggedInCat2, loggedInCat3, loggedInCat4, loggedInCat5, loggedInCat6};
        int[] catOrder = new int[6];
        Label[] catPercentArray = new Label[]{loggedInCat1PercentLabel, loggedInCat2PercentLabel, loggedInCat3PercentLabel, loggedInCat4PercentLabel, loggedInCat5PercentLabel, loggedInCat6PercentLabel};
        ObservableList<String> categoryChoiceBoxList = FXCollections
                .observableArrayList();
        HashMap<Integer, Double> categoriesTotal = new HashMap<Integer, Double>();
        HashMap<Integer, Double> categoriesSpent = new HashMap<Integer, Double>();
        Double budgetTotal = 0.00;
        Double budgetSpent = 0.00;
        try {
            ps = connection.prepareStatement("SELECT * FROM category WHERE userid = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            int cat = 0;

            while (rs.next()) {
                String catname = rs.getString("categoryname");
                categoryChoiceBoxList.add(catname);
                int categoryid = rs.getInt("categoryid");
                catOrder[cat] = categoryid;
                categoriesSpent.put(categoryid, 0.00);
                catArray[cat++].setText(catname);
            }

            ps = connection.prepareStatement("SELECT * FROM budget WHERE userid = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            Double amount = null;
            while (rs.next()) {
                amount = rs.getDouble("amount");
                categoriesTotal.put(rs.getInt("categoryid"), amount);
                budgetTotal += amount;
            }
                cat = 0;
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            while (categoriesTotal.size() > cat) {
                        pieChartData.add(new PieChart.Data(catArray[cat].getText(), categoriesTotal.get(catOrder[cat++])/budgetTotal * 100));
            }
            pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(
                    data.getName(), " amount: ", data.pieValueProperty()
            )));
            pieChart.getData().addAll(pieChartData);


            ps = connection.prepareStatement("SELECT * FROM transaction WHERE userid = ? AND date BETWEEN ? AND ?");
            ps.setInt(1, Main.userid);
            LocalDate currentDate = LocalDate.now();
            LocalDate dateOneMonthFromNow = currentDate.plusMonths(-1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateNow = currentDate.format(formatter);
            String formattedDateOneMonth = dateOneMonthFromNow.format(formatter);
            ps.setDate(2, Date.valueOf(formattedDateOneMonth));
            ps.setDate(3, Date.valueOf(formattedDateNow));
            rs = ps.executeQuery();
            while (rs.next()) {
                amount = rs.getDouble("amount");
                categoriesSpent.put(rs.getInt("categoryid"), categoriesSpent.getOrDefault(rs.getInt("categoryid"), 0.00) + amount);
                budgetSpent += amount;
            }
            ps = connection.prepareStatement("SELECT * FROM recurringexpense WHERE permonth = TRUE AND userid = ?");
            ps.setInt(1, Main.userid);
            rs = ps.executeQuery();
            while (rs.next()) {
                amount = rs.getDouble("amount");
                categoriesSpent.put(rs.getInt("categoryid"), categoriesSpent.getOrDefault(rs.getInt("categoryid"), 0.00) + amount);
                budgetSpent += amount;
            }

            cat = 0;
            Double total = 0.00;
            Double spent = 0.00;
            int used = 0;
            while (cat < catOrder.length) {
                if (cat < categoriesTotal.size()) {
                    total = categoriesTotal.getOrDefault(catOrder[cat], 0.00);
                    spent = categoriesSpent.getOrDefault(catOrder[cat], 0.00);
                    used = (int) Math.ceil((spent / total) * 100);
                    catPercentArray[cat++].setText(used + "%");
                }
                else {
                    catPercentArray[cat++].setText("0%");
                }
            }

            totalBudgetUsed.setText((int) Math.ceil((budgetSpent/budgetTotal) * 100) + "%");


        } catch (Exception e) {
            e.printStackTrace();
        }
        addTransCatChoiceBox.setValue("Category");
        addTransCatChoiceBox.setItems(categoryChoiceBoxList);


    }
}
