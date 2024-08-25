module hr.algebra.trivialpursuit.trivialpursuit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens hr.algebra.trivialpursuit.trivialpursuit to javafx.fxml;
    exports hr.algebra.trivialpursuit.trivialpursuit;
}