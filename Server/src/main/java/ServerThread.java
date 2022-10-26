import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;


public class ServerThread extends Thread {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    private  static DbFunctions dataBase  = new DbFunctions();
    private static Connection connection;
    private static String previousRequest = " ";
    public ServerThread(Socket socket) {

        connection = dataBase.connectToDb("LoginSys","postgres","1q2w3e");
        try {
            //For receiving and sending data
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Packet receivedPacket = (Packet) this.in.readObject();
                System.out.println("Received: " + receivedPacket.message);
                execute(receivedPacket.message);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void execute(String message) {

        Packet packet = new Packet("Start");

        switch (previousRequest) {
            case "Login": {
                //
                if(dataBase.search_by_name(connection,"user","username", message) == true)
                 {
                    packet = new Packet("Insert your password");
                    previousRequest = "Password";
                }
                else
                    {

                        message = "LoginFailed";
                        previousRequest="";
                    }
                break;
            }
            case "Password": {
                //
                if(dataBase.search_by_name(connection,"user","password", message) == true)
                {
                    packet = new Packet("Logged in succesfully!");
                    previousRequest = "Logged";
                }
                else
                {

                    message = "LoginFailed";
                    previousRequest="";
                }

                break;
            }
            default:
                break;
        }


        switch (message)
        {
            case "Login": {

                packet = new Packet("Insert your username");
                previousRequest = message;
                break;
            }
            case "Hello":{
                packet= new Packet("Sall fra!");
                previousRequest = message;
                break;

            }
            case "LoginFailed":
            {
                packet = new Packet("Wrong credentials!");
                break;
            }
            case "Exit": {
                packet = new Packet("Login\nForgot your password\nSign up\n");
                break;
            }
            default : {
                break;
            }
        }

        try {
            this.out.writeObject(packet);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
