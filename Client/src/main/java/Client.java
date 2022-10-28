import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final int PORT = 6543;

    public void start() throws Exception {
        System.out.println("Welcome to our application, please choose an option: ");
        System.out.println("Login");
        System.out.println("Forgot your password");
        System.out.println("SignUp");
        Socket socket = null;

        socket = new Socket("localhost", PORT);

        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        //For receiving and sending data
        boolean isClose = false;
        while (!isClose) {


            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();

            if (message.equals("Bye")) {
                isClose = true;
            }

            Packet packet = new Packet(message);
            outputStream.writeObject(packet);
            Packet recivePacket = (Packet) inputStream.readObject();
            System.out.println(recivePacket.message);
            switch (recivePacket.message)
            {
                case "Logged in succesfully!":
                {
                    System.out.println("Exit");
                    break;
                }
                case "Sucessfully registred!":
                {
                    System.out.println("Login");
                    System.out.println("Forgot your password");
                    System.out.println("SignUp");
                    break;
                }
                case "Password changed sucessfully":
                {
                    System.out.println("Login");
                    System.out.println("Forgot your password");
                    System.out.println("SignUp");
                    break;
                }
                case "Wrong credentials!":
                {

                    System.out.println("Login");
                    System.out.println("Forgot your password");
                    System.out.println("SignUp");
                    break;
                }
                case "Start":
                {
                    System.out.println("Login");
                    System.out.println("Forgot your password");
                    System.out.println("SignUp");
                    break;
                }
                default:
                    break;
            }

        }
        socket.close();
    }
}
