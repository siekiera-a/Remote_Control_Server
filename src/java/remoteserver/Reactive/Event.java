package remoteserver.Reactive;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private final List<Subscriber> subscribers;

    public Event(List<Subscriber> subscribers) {
        if (subscribers != null) {
            this.subscribers = new ArrayList<>(subscribers);
        } else {
            this.subscribers = new ArrayList<>();
        }
    }

    public Event() {
        this.subscribers = new ArrayList<>();
    }

    public void emit() {
        subscribers
            .forEach(Subscriber::onEvent);
    }

    public void subscribe(Subscriber subscriber) {
        if (subscriber != null) {
            subscribers.add(subscriber);
        }
    }

    public void unsubscribe(Subscriber subscriber) {
        if (subscriber != null) {
            subscribers.remove(subscriber);
        }
    }

}
