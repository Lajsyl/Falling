package dat367.falling.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class NotificationManager {

    private static Map<String, List<EventHandler>> map = new HashMap<String, List<EventHandler>>();

    public static <D> void addObserver(String id, EventHandler<D> target) {
        if (!map.containsKey(id)) {
            map.put(id, new ArrayList<EventHandler>());
        }

        map.get(id).add(target);
    }

    public static <D> void registerEvent(String id, D data) {
        Event<D> event = new Event<D>(data);
        List<EventHandler> handlerList = map.get(id);
        if (handlerList != null) {
            try {
                for (EventHandler handler : handlerList) {
                    @SuppressWarnings("unchecked")
                    EventHandler<D> specificEventHandler = (EventHandler<D>) handler;
                    specificEventHandler.handleEvent(event);
                }
            } catch (ClassCastException e) {
                throw new NotificationException("Event handler of incorrect type called. This probably happened because " +
                        "someone registered themselves as a listener for a specific event with an incorrect handler. " +
                        "It is also possible that someone registered an event with the incorrect type of data.");
            }
        }
    }

    private static final class NotificationException extends RuntimeException {
        public NotificationException(String message) {
            super(message);
        }
    }

    public static final class Event<D>{
        public Event(D data) {
            this.data = data;
        }
        public final D data;
    }

    public interface EventHandler<T>{
        public void handleEvent(Event<T> event);
    }

}
