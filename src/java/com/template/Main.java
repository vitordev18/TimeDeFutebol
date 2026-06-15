package com.template;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Lê o arquivo "main.fxml" e converte em objetos do Java.
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));

        // O conteúdo. Recebe o layout do FXML e carrega a tela.
        Scene scene = new Scene(loader.load(), 1920, 1080);

        stage.setTitle("CRUD Times");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}