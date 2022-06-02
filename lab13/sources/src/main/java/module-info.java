module pl.edu.pwr.lab13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens pl.edu.pwr.lab13 to javafx.fxml;
    exports pl.edu.pwr.lab13;
}