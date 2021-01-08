package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exceptions.BorrowException;
import main.server.models.members.Subscriber;

import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Stream;

/** ReturnService : A service for return back a document
 *  Ask if the document is degraded
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class ReturnService extends LibraryService implements Runnable{

    public ReturnService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        boolean success = false;
        do {
            try {
                GeneralDocument doc = requestDocument();

                String response
                        = requestInput(new String[]{"Y", "N"}, "Is the document is degraded ? (Y/N)");

                doc.setDegraded(response.equalsIgnoreCase("Y"));

                doc.returnBack();

                send("The document : " + doc + " has been successfully returned");
                response =
                        requestInput(new String[]{"Y", "N"},"Do you want to return another document ? (Y/N)!");
                success = !response.equalsIgnoreCase("y");
            } catch(BorrowException e1 ) {
                send("error : " + e1.getMessage());
            }
        } while (!success);
        send("Bye ;)");
    }
}
