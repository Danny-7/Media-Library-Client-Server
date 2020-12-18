package main.server.models.utils;

import main.server.models.members.Subscriber;

import java.util.TimerTask;

public class AutomatedBorrowSchedule extends TimerTask {
    private final Subscriber sub;

    public AutomatedBorrowSchedule(Subscriber sub) {
        this.sub = sub;
    }

    @Override
    public void run() {
        sub.suspend();
    }
}
