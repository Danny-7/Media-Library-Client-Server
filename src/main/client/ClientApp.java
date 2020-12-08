package main.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    private static int PORT = 4000;
    private static final String HOST = "localhost";

    private static ObjectInputStream socketIn;
    private static ObjectOutputStream socketOut;

    public static void main(String[] args) {

        try {
            Scanner scanIn = new Scanner(new InputStreamReader(System.in));

            System.out.println("Enter a port connection (4000 default)");

            if(scanIn.hasNext())
                PORT = scanIn.nextInt();

            Socket socket = new Socket(HOST, PORT);

            socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            String serverMessage;
            String clientResponse;

            while(true) {
                serverMessage = (String)socketIn.readObject();
                System.out.println(serverMessage);
                clientResponse = scanIn.nextLine();
                socketOut.writeObject(clientResponse);
            }



        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
