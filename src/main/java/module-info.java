module dev.gustavoesposar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens dev.gustavoesposar to javafx.fxml;
    exports dev.gustavoesposar;
}
