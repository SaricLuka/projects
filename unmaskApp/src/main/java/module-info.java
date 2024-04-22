module hr.javafx.unmask.unmaskApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires bcrypt;
    requires java.sql;

    opens hr.javafx.unmask.unmaskapp to javafx.fxml;
    exports hr.javafx.unmask.unmaskapp;
    opens hr.javafx.unmask.unmaskapp.model to javafx.base;
    opens hr.javafx.unmask.unmaskapp.interfaces to javafx.base;
}