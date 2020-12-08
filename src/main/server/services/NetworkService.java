package main.server.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class NetworkService {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public NetworkService(Socket socket) {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("An error occurred while we tried to communicate with the server");
        }
    }

    public void send(String message) {
        try {
            assert false;
            out.writeObject(message);
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
