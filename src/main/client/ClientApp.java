package main.client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/** ClientApp : A client app where you can connect to a local server (port) and interact with
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class ClientApp {
    private static int PORT = 4000;
    private static final String HOST = "localhost";

    public static void main(String[] args) {

        try {
            Scanner scanIn = new Scanner(new InputStreamReader(System.in));

            System.out.println(
                    """
                            Welcome to our awesome application !

                            Choose which server you want to connect
                            \t3000 -> Reserve a document
                            \t4000 -> Borrow a document
                            \t5000 -> Return back a document
                            """
            );

            String[] serversPort = {"3000", "4000", "5000"};
            String line;

            do {
                System.out.println("Enter a connection port (4000 default)");
                line = scanIn.nextLine();
                if(line.isBlank())
                    break;
            }while(Arrays.stream(serversPort).noneMatch(line::equalsIgnoreCase));

            if(!line.isBlank())
                PORT = Integer.parseInt(line);

            Socket socket = new Socket(HOST, PORT);
            // end points to interact with the server
            ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

            String clientResponse;

            ServerListener serverListener = new ServerListener(socketIn);
            // thread which collect data from the server
            serverListener.start_thread();

            while(true) {
                while((clientResponse = scanIn.nextLine()).isEmpty());
                socketOut.writeObject(clientResponse);
                socketOut.flush();
                socketOut.reset();
            }
        } catch (IOException e) {
            System.out.println("Server is not reachable or still initializing, please retry in a few seconds");
        }
    }
}
