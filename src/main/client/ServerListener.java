package main.client;

import java.io.IOException;
import java.io.ObjectInputStream;

/** ServerListener : A listener for the data sent by the server
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
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
                // read serializable data sent by the server
                serverMessage = (String) socketIn.readObject();
                System.out.println(serverMessage);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                isActive = false;
            }
        }
    }
}
