package main.server;

import main.server.services.BorrowService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BorrowServer implements Runnable {
    private final static String suffix = "[BorrowServer]";
    private final static int port = 4000;
    ServerSocket ssocket;

    public BorrowServer() {
        System.out.println(suffix + " Listening on port " + port + "...");
        try {
            this.ssocket = new ServerSocket(port);
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
                new Thread(new BorrowService(csocket)).start();

            } catch (IOException e) {
                //Handle exception
                e.printStackTrace();
            }
        }
    }
}
