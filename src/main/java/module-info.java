module dev.gustavoesposar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires bcrypt;
    requires org.apache.pdfbox;
    requires java.desktop;  // Adicione esta linha para incluir o módulo java.desktop

    opens dev.gustavoesposar.controller to javafx.fxml;
    opens dev.gustavoesposar.controller.abstracts to javafx.fxml;
    opens dev.gustavoesposar.model to javafx.base;
    exports dev.gustavoesposar;
}
