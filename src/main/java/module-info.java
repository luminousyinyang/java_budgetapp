module budgetapp.java_budgetapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.jfoenix;

    opens budgetapp.java_budgetapp.Controllers to javafx.fxml;

    opens budgetapp.java_budgetapp.HelperClasses to javafx.base;
    exports budgetapp.java_budgetapp;
}