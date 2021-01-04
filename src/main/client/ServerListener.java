package main.client;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ServerListener implements Runnable {
    private ObjectInputStream socketIn;

    public ServerListener(ObjectInputStream socketIn) {
        this.socketIn = socketIn;
    }

    public void start_thread() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        boolean isActive = true;
        while (isActive) {
            String serverMessage;
            try {
                serverMessage = (String) socketIn.readObject();
                System.out.println(serverMessage);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                isActive = false;
            }
        }
    }
}
