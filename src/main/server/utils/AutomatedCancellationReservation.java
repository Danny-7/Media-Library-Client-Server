package main.server.utils;

import main.server.models.Document;

import java.util.TimerTask;

/** AutomatedCancellationReservation : Schedule an automated task when someone reserve a document
 *  On run it return back the document because the reservation time was exceeded
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class AutomatedCancellationReservation extends TimerTask {
    private final Document doc;

    public AutomatedCancellationReservation(Document doc) {
        this.doc = doc;
    }

    @Override
    public void run() {
        doc.returnBack();
    }
}
