package main.server.utils;

import main.server.models.Document;
import main.server.models.members.Subscriber;

import java.util.TimerTask;

/** AutomatedBorrowSchedule : Schedule an automated task when someone borrow a document
 *  On run it suspend the subscriber and return back the document
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class AutomatedBorrowSchedule extends TimerTask {
    private final Subscriber sub;
    private final Document doc;

    public AutomatedBorrowSchedule(Subscriber sub, Document doc) {
        this.sub = sub;
        this.doc = doc;
    }

    @Override
    public void run() {
        sub.suspend();
        doc.returnBack();
    }
}
