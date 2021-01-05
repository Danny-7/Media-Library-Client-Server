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
        try {
            GeneralDocument doc = requestDocument();

            String response;
            String[] choices = {"Y", "N"};
            do {
                send("Is the document is degraded ? (Y/N)");
                response = (String)read();
                // check if the value read is on the choices array stream
            }while(Arrays.stream(choices).noneMatch(response::equalsIgnoreCase));

            doc.setDegraded(response.equalsIgnoreCase("Y"));

            doc.returnBack();

            send("The document : " + doc + " has been successfully returned");

        } catch(BorrowException e1 ) {
            send("error : " + e1.getMessage());
        }
    }
}
