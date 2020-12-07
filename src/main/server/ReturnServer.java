package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReturnServer implements Runnable {
    private final static String suffix = "[ReturnServer]";
    private final static int port = 5000;
    ServerSocket ssocket;

    public ReturnServer() {
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
