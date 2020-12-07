package main.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReturnApp {

    private static String HOST = "127.0.0.1";
    private static int PORT = 4000;

    public static void main(String[] args) {
        try {
            //On ouvre la connexion au serveur
            Socket sserver = new Socket(HOST, PORT);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(sserver.getInputStream()));
            PrintWriter socketOut = new PrintWriter(sserver.getOutputStream(), true);

            System.out.println("Connexion au serveur " + HOST + ":" + PORT + " effectuée");

            //TRAITEMENT

            //Fermeture du socket
            sserver.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
