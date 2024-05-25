package dev.gustavoesposar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import dev.gustavoesposar.database.DatabaseManager;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) throws IOException {
        App.stage = stage;
    
        loadLoginScene();
        configureStage();
        
        stage.show();
        setOnCloseEventHandler();
    }
    
    private void loadLoginScene() throws IOException {
        scene = new Scene(loadFXML("pets"));
        stage.setScene(scene);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    private void configureStage() {
        stage.setResizable(false);
        stage.setTitle("Login do Sistema");
        stage.getIcons().add(new Image("file:src/main/resources/dev/gustavoesposar/img/logo_petshop.png"));
        stage.setOnCloseRequest(event -> {
            try {
                DatabaseManager.fecharConexao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void setOnCloseEventHandler() {
        stage.setOnCloseRequest(event -> {
            try {
                DatabaseManager.fecharConexao();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setNewScene(String fxml, String title) throws IOException {
        scene = new Scene(loadFXML(fxml));
        stage.setScene(scene);
        stage.setTitle(title);
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch();
    }

}