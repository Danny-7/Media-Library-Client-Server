package main.server.utils;

import main.server.models.Document;

import java.util.TimerTask;

public class AutomatedCancellationReservation extends TimerTask {
    private Document doc;

    public AutomatedCancellationReservation(Document doc) {
        this.doc = doc;
    }

    @Override
    public void run() {
        doc.returnBack();
    }
}
