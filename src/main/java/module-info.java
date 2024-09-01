module hr.algebra.trivialpursuit.trivialpursuit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens hr.algebra.trivialpursuit.trivialpursuit to javafx.fxml;
    exports hr.algebra.trivialpursuit.trivialpursuit;
    exports hr.algebra.trivialpursuit.trivialpursuit.model;
    opens hr.algebra.trivialpursuit.trivialpursuit.model to javafx.fxml;
    exports hr.algebra.trivialpursuit.trivialpursuit.view;
    opens hr.algebra.trivialpursuit.trivialpursuit.view to javafx.fxml;
}