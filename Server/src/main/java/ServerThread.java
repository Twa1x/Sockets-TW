import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class ServerThread extends Thread {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public ServerThread(Socket socket) {
        try {
            //For receiving and sending data
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Packet receivedPacket = (Packet) this.in.readObject();
            System.out.println("Received: " + receivedPacket.message);
            execute(receivedPacket.message);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void execute(String message) {
<<<<<<< Updated upstream
        Packet packet = switch (message) {
            case "Hello" -> new Packet("Hello There");
            case "How are you?" -> new Packet("I'm fine");
            case "Bye" -> new Packet("Bye");
            default -> new Packet("Can't understand you :/");
        };
=======
        Packet packet = new Packet("Nothing");

        switch (previousRequest) {
            case "Login": {
                if(message.equals("Alex")) {
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
                if(message.equals("Parola")){
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
                packet= new Packet("Sall!");
                previousRequest = message;
                break;

            }
            case "LoginFailed":
            {
                packet = new Packet("Wrong credentials!");
                break;
            }
            default : {
                break;
            }
        }
>>>>>>> Stashed changes

        try {
            this.out.writeObject(packet);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
