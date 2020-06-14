package sample;

import java.io.IOException;

import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Chatbox extends Application {


    BorderPane root = new BorderPane();
    BorderPane main = new BorderPane();
    //Chatbox
    ScrollPane scrollbase = new ScrollPane();

    public static VBox chatBox = new VBox();
    TextField chatInput = new TextField();
    private static sample.Client client;


    @Override
    public void start(Stage primaryStage) throws Exception {


        client = new sample.Client();
        chatBox.setId("chatbox");
        scrollbase.setContent(chatBox);
        scrollbase.setId("scrollbase");
        scrollbase.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollbase.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatInput.textProperty().addListener((observable, ov, nv) -> {
            if (nv.length() > 29) {
                chatInput.setText(ov);
            }
        });
        // when enter is pressed, send text that is in the input box, update chatbox as well
        chatInput.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                updateChat("Me: " + chatInput.getText(), "me-texts");
                //connection.send("MESSAGE" + chatInput.getText());

                client.send(chatInput.getText());
                chatInput.setText("");
                scrollbase.setVvalue(1.0);
            }
        });

        scrollbase.fitToHeightProperty().set(true);

        main.setCenter(scrollbase);
        main.setBottom(chatInput);
        main.setId("centre");

        VBox leftcolumn = new VBox();
        leftcolumn.setId("left");
        leftcolumn.setAlignment(Pos.CENTER);
        Circle profile = new Circle(30);
        profile.setId("yourProfileCircle");
        leftcolumn.getChildren().add(profile);

        HBox top = new HBox();
        top.setId("top");
        HBox bottom = new HBox();
        bottom.setId("bottom");


        root.setBottom(bottom);
        root.setCenter(main);
        root.setLeft(leftcolumn);
        root.setTop(top);
        root.getStylesheets().addAll("sample/Client/stylesheet.css");


        Scene scene = new Scene(root);
        primaryStage.setTitle("Chatbox");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    public static void updateChat(String text, String style) {
        Label label = new Label(text);
        label.setId("textLabel");
        chatBox.getChildren().add(label);
        // if chatbox exceeds 25 messages, remove the top one
        if (chatBox.getChildren().size() > 25) {
            ArrayList<Node> nodes = new ArrayList<>(chatBox.getChildren());
            nodes.remove(0);
            chatBox.getChildren().clear();
            chatBox.getChildren().addAll(nodes);
        }
    }

    public static class Command {
        public void update(String message) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                updateChat(message, "me-style");
                }
            });

            //System.out.println(message);

        }

    }

    public static void main(String[] args) throws IOException{
        launch(args);





    }

}
