package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Base64.Decoder;

public class Client
{
    private final PrintWriter output;
    private final sample.Chatbox.Command command = new sample.Chatbox.Command();
    Socket socket = new Socket("127.0.0.1", 1234);
    

    public Client() throws IOException {
        // establish connection details of server socket
        // create input and output streams from and to server
        Scanner dis= new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);


        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        while (dis.hasNextLine()) {
                            // read the message sent to this client

                            String msg = dis.nextLine();
                            if (msg.startsWith("INNNNN")){
                                String[] parts = msg.split("-");
                                String part1 = parts[0]; // 004
                                String part2 = parts[1];
                                command.updateClientNo(part2);
                            }
                            else if (msg.startsWith("NOTHERE")){
                                System.out.println("Have been here");
                                String message = msg.substring(7);

                                String []senderList = message.split(";;;;;;;;;;;;;;;;;;;;;;;;;;;");
                                String sender = senderList[0];
                                String MsgToSend = senderList[1];
                                command.update(sender + ":"+MsgToSend);
                                System.out.println("here");
                                System.out.println(sender + ":"+MsgToSend);
                            }
                            else if (msg.startsWith("SENDMESSAGE//")){

                            }
                            else{
                                String[] parts = msg.split("//////////////////////////");
                                String part1 = parts[0];
                                String part2 = parts[1];
                                byte[] decodedBytes = Base64.getDecoder().decode(part2);
                                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                                BufferedImage proPic= ImageIO.read(bis);
                                Image image = SwingFXUtils.toFXImage(proPic, null);
                                command.updateImage(part1,image);

                            }


                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }
        });
        readMessage.start();

    }
    public void send(String value) {
        System.out.println(value);
        output.println(value);
    }
    public void sendImage(File file,String name) throws IOException, InterruptedException {

        String base64Image = "";
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);

            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        String something = name+"//////////////////////////"+base64Image;
        output.println(something);

    }

}


