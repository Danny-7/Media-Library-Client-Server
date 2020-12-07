package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReservationServer implements Runnable {
    private final static String suffix = "[ReservationServer]";
    private final static int port = 3000;
    ServerSocket ssocket;

    public ReservationServer() {
        System.out.println(suffix + " Listening on port " + this.port + "...");
        try {
            this.ssocket = new ServerSocket(this.port);
        } catch (IOException e) {
            //Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                Socket csocket = ssocket.accept();
                System.out.println(suffix + " request received from " + csocket.getInetAddress());

                //Service

            } catch (IOException e) {
                //Handle exception
                e.printStackTrace();
            }
        }
    }
}
