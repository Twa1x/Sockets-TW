import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Pack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;


public class ServerThread extends Thread {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    private  static DbFunctions dataBase  = new DbFunctions();

    private static Questions questions = new Questions();
    private  static String userName = "";
    private static Connection connection;

    private static  User user = new User();
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
            case "Start" :
            {
                packet = new Packet("Start");
                break;
            }
            case "Login": {
                //

                if(dataBase.searchByName(connection,"user","username", message) == true)
                {
                    userName = message;
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
                if(dataBase.searchByPassword(connection,"user","password", message, userName) == true)
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
            case "SignUp":
            {

                if (message.length()<=5)
                {
                    packet= new Packet("The username has to be longer than 5 characters!\nPlease insert another username: ");
                    previousRequest = "SignUp";
                }
                else{
                if(dataBase.searchByName(connection,"user","username", message) == true)
                {
                    packet = new Packet("This username is not available! \nPlease insert another one: ");

                }
                else
                {
                user.setUsername(message);
                packet = new Packet("Please set your password:");
                    previousRequest="SignUpQuestion";}
                }
                break;
            }
            case "SignUpQuestion":
            {
                if (message.length()<=5)
                {
                    packet= new Packet("The password has to be longer than 5 characters!\nPlease insert another password: ");
                    previousRequest = "SignUpQuestion";
                }
                else{
                String question = questions.getRandomQuestion();
                user.setPassword(message);
                packet = new Packet(question);
                user.setQuestion(question);
                previousRequest = "SignUpAnswer";
                }
                break;
            }
            case "SignUpAnswer":
            {


                user.setAnswer(message);
                packet = new Packet("Sucessfully registred!");
                previousRequest = "Start";
                dataBase.insertUser(connection,"user", user);
                break;
            }
            case "Forgot your password":
            {
                userName = message;
                String tempString = dataBase.getColumn(connection,userName, "question");
                if( tempString.equals(""))
                    packet = new Packet("Wrong credentials!");
                else
                    packet = new Packet(dataBase.getColumn(connection,userName, "question"));
                previousRequest =  "Validating Answer";
                break;
            }
            case "Validating Answer":
            {
                if(message.equals(dataBase.getColumn(connection,userName,"answer")))
                {packet = new Packet("Insert your new password");
                    previousRequest = "New Password";
                }

                else
                    packet = new Packet("Wrong credentials!");
                break;
            }
            case "New Password":
            {
                if (message.length()<=5)
                {
                    packet= new Packet("The password has to be longer than 5 characters!\nPlease insert another password: ");
                    previousRequest = "New Password";
                }else
                {
                dataBase.updatePassword(connection,userName, message);
                packet = new Packet("Password changed sucessfully");
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
            case "SignUp": {
                packet = new Packet("Please set your username: ");
                previousRequest=message;
                break;
            }
            case "Forgot your password":
            {
                packet = new Packet("Insert your username");
                previousRequest =  "Forgot your password";
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
