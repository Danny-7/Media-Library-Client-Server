package main.server;

import main.server.services.ReservationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** ReservationServer : Server for reserve a document
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class ReservationServer implements Runnable {
    private final static String suffix = "[ReservationServer]";
    private final static int port = 3000;
    ServerSocket ssocket;

    public ReservationServer() {
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
                new Thread(new ReservationService(csocket)).start();
            } catch (IOException e) {
                //Handle exception
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
}
