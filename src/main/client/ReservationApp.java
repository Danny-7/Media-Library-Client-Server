package main.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ReservationApp {

    private static String HOST = "127.0.0.1";
    private static int PORT = 3000;

    public static void main(String[] args) {
        try {
            //On ouvre la connexion au serveur
            Socket sserver = new Socket(HOST, PORT);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(sserver.getInputStream()));
            PrintWriter socketOut = new PrintWriter(sserver.getOutputStream(), true);

            System.out.println("Connexion au serveur " + HOST + ":" + PORT + " effectuée");

            Scanner sc = new Scanner(new InputStreamReader(System.in));
            boolean estConnecté = true;
            while(estConnecté) {
                String catalogue = socketIn.readLine();
                System.out.println(catalogue);
            }

            //Fermeture du socket
            sserver.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
