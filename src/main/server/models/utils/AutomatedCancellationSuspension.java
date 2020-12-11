package main.server.models.utils;

import main.server.models.members.Subscriber;

import java.util.TimerTask;

public class AutomatedCancellationSuspension extends TimerTask {
    private final Subscriber sub;

    public AutomatedCancellationSuspension(Subscriber sub) {
        this.sub = sub;
    }

    @Override
    public void run() {
        sub.unSuspend();
    }
}
