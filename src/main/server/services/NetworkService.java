package main.server.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/** NetworkService : A service for the network operation
 * Send to the client and read an object serializable from the client
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class NetworkService {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public NetworkService(Socket socket) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("An error occurred while we tried to communicate with the server");
        }
    }

    public void send(String message) {
        try {
            assert false;
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Serializable read() {
        Serializable readObject = null;
        try {
            assert false;
            readObject = (Serializable) in.readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
        return readObject;
    }
}
