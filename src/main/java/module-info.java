module dev.gustavoesposar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires bcrypt;

    opens dev.gustavoesposar.controller to javafx.fxml;
    opens dev.gustavoesposar.controller.abstracts to javafx.fxml;
    opens dev.gustavoesposar.model to javafx.base;
    exports dev.gustavoesposar;
}
