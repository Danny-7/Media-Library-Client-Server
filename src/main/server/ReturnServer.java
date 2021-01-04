package main.server;

import main.server.services.ReturnService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReturnServer implements Runnable {
    private final static String suffix = "[ReturnServer]";
    private final static int port = 5000;
    ServerSocket ssocket;

    public ReturnServer() {
        System.out.println(suffix + " Listening on port " + port + "...");
        try {
            this.ssocket = new ServerSocket(port);
        } catch (IOException e) {
            //Handle exception
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                Socket csocket = ssocket.accept();
                System.out.println(suffix + " request received from " + csocket.getInetAddress());
                new Thread(new ReturnService(csocket)).start();
            } catch (IOException e) {
                //Handle exception
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
}
