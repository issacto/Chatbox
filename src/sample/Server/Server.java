
import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server
{

    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();

    // counter for clients
    static int i = 0;

    public static void main(String[] args) throws IOException
    {
        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);

        Socket s;

        // running infinite loop for getting
        // client request
        while (true)
        {
            // Accept the incoming request
            s = ss.accept();

            System.out.println("New client request received : Number" + s);

            // obtain input and output streams
            Scanner dis = new Scanner(s.getInputStream());
            PrintWriter dos = new PrintWriter(s.getOutputStream());

            System.out.println("Creating a new handler for this client...");


            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(s, dis, dos);

            for (ClientHandler mc : Server.ar) {
                mtch.output.println("INNNNN-"+ mc.name);
                if (mc.imageString!=null){
                    mtch.output.println(mc.imageString);
                }
            }

            System.out.println("Name is " +mtch.name);

            // Create a new Thread with this object.
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");

            // add this client to active clients list
            ar.add(mtch);

            // start the thread.
            t.start();

            i++;
            }


    }
}

// ClientHandler class
class ClientHandler implements Runnable {
    public String name;
    public PrintWriter output;
    public String imageString;
    final Scanner dis;
    Socket s;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket s,Scanner dis, PrintWriter dos) {
        this.dis = dis;
        this.output = new PrintWriter(dos, true);
        this.s = s;
        this.isloggedin = true;
        imageString = null;
    }

    @Override
    public void run() {

        while (true) {
            try {
                while (dis.hasNextLine()) {
                    // read the message sent to this client

                    String msg = dis.nextLine();
                    System.out.println(msg);

                    if (msg.equals("logout")) {
                        this.isloggedin = false;
                        this.s.close();
                        break;
                    }
                    // break the string into message and recipient part
                    if( msg.startsWith("SENDMESSAGE//")) {
                        String message = msg.substring(13);
                        String []senderList = message.split(";;;;;;;;;;;;;;;;;;;;;;;;;;;");
                        String recipient = senderList[0];
                        String MsgToSend = senderList[1];
                        for (ClientHandler mc : Server.ar) {
                            if (mc.name.equals(recipient) && mc.isloggedin == true) {
                                mc.output.println("NOTHERE"+ this.name+";;;;;;;;;;;;;;;;;;;;;;;;;;;"+MsgToSend);
                                break;
                            }
                        }
                    }
                    if(msg.startsWith("INNNNN-")){
                        String[] parts = msg.split("-");// 004
                        String part2 = parts[1];
                        this.name =part2;
                        for (ClientHandler mc : Server.ar) {
                            if (mc.name!=this.name) {
                                mc.output.println(msg);
                            }
                        }
                    }
                    else{
                        String[] parts = msg.split("//////////////////////////");
                        String part1 = parts[0];
                        imageString = msg;
                        for (ClientHandler mc : Server.ar) {
                            if (mc.name!=part1) {
                                mc.output.println(msg);
                            }
                        }
                    }


                }
            } catch (IOException e) {

                e.printStackTrace();
            }


            try {
                // closing resources
                this.dis.close();
                output.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
