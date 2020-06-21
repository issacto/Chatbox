package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Chatbox;

import java.awt.*;
import java.util.ArrayList;

public class UserLogIn extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setPrefSize(300,350);
        Text scenetitle = new Text("Plase enter your username to join the Chatbox!");
        TextField firstNameFill = new TextField();
        scenetitle.relocate(23,170);


        Button btn = new Button("Enter");
        btn.setOnAction((EventHandler<ActionEvent>) e -> {
                String content = firstNameFill.getText();
                System.out.println(content);
                Chatbox chatbox = new Chatbox(content);
            try {
                chatbox.start(primaryStage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });
        firstNameFill.relocate(70,200);

        btn.relocate(125,250);
        root.getChildren().addAll(btn,firstNameFill,scenetitle);

        Scene scene = new Scene(root);
        primaryStage.setTitle("UserLogin");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
