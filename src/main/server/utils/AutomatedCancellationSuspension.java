package main.server.utils;

import main.server.models.members.Subscriber;

import java.util.TimerTask;

/** AutomatedCancellationSuspension : Schedule an automated task when someone is suspended
 *  On run it unsuspend the subscriber
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
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
