package dat367.falling.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {

    private Map<String, List<EventHandler>> map = new HashMap<String, List<EventHandler>>();

    private static NotificationManager notificationManager;

    public <D> void addObserver(String id, EventHandler<D> target) {
        if (!map.containsKey(id)) {
            map.put(id, new ArrayList<EventHandler>());
        }

        map.get(id).add(target);
    }

    public <D> void registerEvent(String id, D data) {
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

    public <D> void clear() {
        map.clear();
    }

    private final class NotificationException extends RuntimeException {
        public NotificationException(String message) {
            super(message);
        }
    }

    public final class Event<D>{
        public Event(D data) {
            this.data = data;
        }
        public final D data;
    }

    public interface EventHandler<T>{
        public void handleEvent(Event<T> event);
    }

    public static NotificationManager getDefault(){
        if (notificationManager == null){
            notificationManager = new NotificationManager();
        }
        return notificationManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationManager that = (NotificationManager) o;

        return map != null ? map.equals(that.map) : that.map == null;

    }

    @Override
    public int hashCode() {
        return map != null ? map.hashCode() : 0;
    }
}
