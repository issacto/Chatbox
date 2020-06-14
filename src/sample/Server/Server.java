
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
            ClientHandler mtch = new ClientHandler(s,"client "+ i, dis, dos);

            // Create a new Thread with this object.
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");

            // add this client to active clients list
            ar.add(mtch);

            // start the thread.
            t.start();

            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;

        }
    }
}

// ClientHandler class
class ClientHandler implements Runnable {
    Scanner scn = new Scanner(System.in);
    private String name;
    private PrintWriter output;
    final Scanner dis;
    Socket s;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket s, String name,
                         Scanner dis, PrintWriter dos) {
        this.dis = dis;
        output = new PrintWriter(dos, true);
        this.name = name;
        this.s = s;
        this.isloggedin = true;
    }

    @Override
    public void run() {

        String received;
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
                    StringTokenizer st = new StringTokenizer(msg, "#");
                    String MsgToSend = st.nextToken();
                    String recipient = st.nextToken();



                    for (ClientHandler mc : Server.ar) {
                        // if the recipient is found, write on its
                        // output stream


                        if (mc.name.equals(recipient) && mc.isloggedin == true) {
                            mc.output.println(this.name + " : " + MsgToSend);
                            break;
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
