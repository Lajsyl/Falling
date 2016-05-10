package dat367.falling.platform_abstraction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class RenderQueue {

    private static Queue<RenderTask> queue = new LinkedList<RenderTask>();
    private static Queue<GUITask> guiQueue = new LinkedList<GUITask>();

    public static void addTask(RenderTask task) {
        queue.add(task);
    }

    public static void addGUITask(GUITask guiTask){
        guiQueue.add(guiTask);
    }

    public static void clear() {
        queue.clear();
        guiQueue.clear();
    }

    public static Iterable<RenderTask> getTasks() {
        return Collections.unmodifiableCollection(queue);
    }

    public static Iterable<GUITask> getGUITasks() {
        return Collections.unmodifiableCollection(guiQueue);
    }

}
