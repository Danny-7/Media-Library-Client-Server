package main.server;

import main.server.services.ReturnService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** ReturnServer : Server for return back a document
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
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
                // accept requests to this server
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
