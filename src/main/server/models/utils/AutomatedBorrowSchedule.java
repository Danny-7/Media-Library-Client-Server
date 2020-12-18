package main.server.models.utils;

import main.server.models.Document;
import main.server.models.members.Subscriber;

import java.util.TimerTask;

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
