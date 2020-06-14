package sample;
import java.io.*;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Client
{
    private final PrintWriter output;
    private final sample.Chatbox.Command command = new sample.Chatbox.Command();

    public Client() throws IOException {
        // establish connection details of server socket
        Socket socket = new Socket("127.0.0.1", 1234);
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

                            command.update(msg);
                            System.out.println(msg);
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

}