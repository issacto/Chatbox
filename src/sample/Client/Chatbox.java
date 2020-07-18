package sample;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javax.imageio.ImageIO;

public class Chatbox extends Application {


    BorderPane root = new BorderPane();
    BorderPane main = new BorderPane();
    static VBox CentreLeft = new VBox();
    static ScrollPane scrollbase = new ScrollPane();

    public VBox chatBox = new VBox();
    TextField chatInput = new TextField();
    private static sample.Client client;
    HBox top = new HBox();
    BorderPane TopLeft = new BorderPane();
    public Image proPic;
    public BufferedImage Propic;
    static ArrayList<User> otherUserList = new ArrayList<User>();
    public static String UserName;
    static HashMap<String, User> capitalCities = new HashMap<String,User>();
    static String nameToSend = new String();

    public Chatbox(String name ){
        UserName = name;
        nameToSend=UserName;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        client = new sample.Client();
        //Centre
        chatBox.setId("chatbox");
        scrollbase.setId("scrollbase");
        scrollbase.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollbase.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatInput.textProperty().addListener((observable, ov, nv) -> {
            if (nv.length() > 60) {
                chatInput.setText(ov);
            }
        });
        // when enter is pressed, send text that is in the input box, update chatbox as well
        chatInput.setOnKeyPressed(e -> {
            if (!capitalCities.isEmpty() & e.getCode().equals(KeyCode.ENTER)) {
                updateChat("Me: " + chatInput.getText(), "me-texts");

                client.send("SENDMESSAGE//"+nameToSend+";;;;;;;;;;;;;;;;;;;;;;;;;;;"+chatInput.getText());
                chatInput.setText("");
                scrollbase.setVvalue(1.0);
            }
        });

        scrollbase.fitToHeightProperty().set(true);
        main.setCenter(scrollbase);
        main.setBottom(chatInput);
        main.setId("centre");


        //left Column
        CentreLeft.setSpacing(10);
        CentreLeft.setPadding(new Insets(10, 0,0,0));
        BorderPane leftcolumn = new BorderPane();
        leftcolumn.setId("left");

        clientUpdate("Yourself");



        Button fileButton = new Button("Change Profile Pic from Computer");

        fileButton.setOnAction((EventHandler<ActionEvent>) e -> {

            FileChooser chooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files","*.jpg");
            chooser.getExtensionFilters().add(extFilter);
            chooser.setTitle("Open File");
            File file = chooser.showOpenDialog(new Stage());
            try {
                Propic = ImageIO.read(file);
                proPic = SwingFXUtils.toFXImage(Propic, null);
                Circle newProfilePic = new Circle(50);
                newProfilePic.setFill(new ImagePattern(proPic));
                capitalCities.get("Yourself").base.getChildren().add(newProfilePic);
                client.sendImage(file,UserName);

            } catch (IOException | InterruptedException ex) {
                System.out.println("This is wrong");
            }

        });

        client.send("INNNNN-"  + UserName);



        top.getChildren().add(fileButton);
        //leftcolumn.setTop(TopLeft);
        leftcolumn.setCenter(CentreLeft);


        //Top and Bottom
        top.setId("top");
        HBox bottom = new HBox();
        bottom.setId("bottom");


        root.setBottom(bottom);
        root.setCenter(main);
        root.setLeft(leftcolumn);
        root.setTop(top);
        root.getStylesheets().addAll("stylesheet.css");


        Scene scene = new Scene(root);
        primaryStage.setTitle(UserName+ "'s Chatbox");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    public static void updateChat(String text, String style) {
        Label label = new Label(text);
        label.setId("textLabel");
        String temName = text.split(":")[0];
        System.out.println(temName);
        for (String name:capitalCities.keySet()){
            if(temName.equals(name)){
                capitalCities.get(name).newChatBox.getChildren().add(label);
                break;
            }


        }

    }

    public static void clientUpdate(String ClientName) {
        User user = new User(ClientName);
        capitalCities.put(ClientName,user);
        BorderPane required = user.getPane();
        otherUserList.add(user);
        CentreLeft.getChildren().add(required);
    }


    public static class Command {
        public void update(String message) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                updateChat(message, "me-style");
                }
            });
        }
        public void updateClientNo(String message) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    clientUpdate(message);
                }
            });
        }
        public void updateImage(String otherName, Image imageOfOthers) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for(User user : otherUserList){
                        if(user.name.equals(otherName)) {
                            CentreLeft.getChildren().remove(user.getPane());
                            StackPane base = new StackPane();
                            Circle newOtherProfilePic = new Circle(50);
                            newOtherProfilePic.setFill(new ImagePattern(imageOfOthers));
                            base.getChildren().add(newOtherProfilePic);
                            user.realBase.setCenter(base);
                            CentreLeft.getChildren().add(user.realBase);
                            break;
                        }
                    }
                }
            });
        }

    }
    public static class User{
        public String name ;
        public VBox newChatBox;
        public BorderPane realBase = new BorderPane();
        public StackPane base = new StackPane();
        public User(String ClientName){
            name = ClientName;
            newChatBox = new VBox();
            newChatBox.setId("centre");
            realBase.setPrefSize(100,100);
            ImageView imageView = new ImageView("user.jpg");
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            Circle profile = new Circle(50);
            profile.setFill(javafx.scene.paint.Color.RED);
            base.getChildren().addAll(profile,imageView);
            realBase.setCenter(base);
            Button nametext = new Button(name);
            nametext.setOnAction((EventHandler<ActionEvent>) e -> {
                scrollbase.setContent(newChatBox);
                nameToSend=name;
                System.out.println("name to send =" + nameToSend);
            });
            realBase.setBottom(nametext);
            realBase.setAlignment(nametext,Pos.CENTER);

        }
        public BorderPane getPane(){
                return realBase;
        }

    }


    public static void main(String[] args) throws IOException{
        launch(args);





    }

}
