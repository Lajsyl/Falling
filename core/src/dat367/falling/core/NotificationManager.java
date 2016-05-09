package dat367.falling.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {

    public static final class Event<D>{
        public Event(D data) {
            this.data = data;
        }
        D data;
    }

    public static interface EventHandler<T>{
        public void handleEvent(Event<T> event);
    }

    public static Map<String, List<EventHandler>> map = new HashMap<String, List<EventHandler>>();

    public static void addObserver(String id, EventHandler target) {
        if (!map.containsKey(id)) {
            map.put(id, new ArrayList<EventHandler>());
        }

        map.get(id).add(target);
    }

    public static <D> void pushEvent(String id, D data) {
        Event<D> event = new Event<D>(data);
        List<EventHandler> handlerList = map.get(id);
        if (handlerList != null) {
            for (EventHandler handler : handlerList) {
                handler.handleEvent(event);
            }
        }
    }



}
