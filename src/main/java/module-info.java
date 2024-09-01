module hr.algebra.trivialpursuit.trivialpursuit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens hr.algebra.trivialpursuit.trivialpursuit to javafx.fxml;
    exports hr.algebra.trivialpursuit.trivialpursuit;
    exports hr.algebra.trivialpursuit.trivialpursuit.view;
    opens hr.algebra.trivialpursuit.trivialpursuit.view to javafx.fxml;
    exports hr.algebra.trivialpursuit.trivialpursuit.repository;
    opens hr.algebra.trivialpursuit.trivialpursuit.repository to javafx.fxml;
    exports hr.algebra.trivialpursuit.trivialpursuit.utils;
    opens hr.algebra.trivialpursuit.trivialpursuit.utils to javafx.fxml;
}