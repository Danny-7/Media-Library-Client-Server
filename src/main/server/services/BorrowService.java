package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exceptions.BorrowException;
import main.server.models.exceptions.SuspensionException;
import main.server.models.members.Subscriber;

import java.net.Socket;

/** BorrowService : A service for borrow a document
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class BorrowService extends LibraryService implements Runnable {

    public BorrowService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        boolean success = false;
        Subscriber sb = requestSubscriber();

        do {
            GeneralDocument doc = requestDocument();
            try {
                doc.borrowBy(sb);
                send("The document : " + doc + " has been successfully borrowed");
                success = true;
            } catch(BorrowException | SuspensionException e1 ) {
                send("error : " + e1.getMessage());
            }
            String response =
                    requestInput(new String[]{"Y", "N"},"Do you want to borrow another document ? (Y/N)!");
            if(response.equalsIgnoreCase("y"))
                success = false;
        } while (!success);
        send("Bye ;)");
    }
}
