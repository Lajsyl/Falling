package dat367.falling.platform_abstraction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class RenderQueue {

    private Queue<RenderTask> queue = new LinkedList<RenderTask>();
    private Queue<GUITask> guiQueue = new LinkedList<GUITask>();

    public void addTask(RenderTask task) {
        queue.add(task);
    }

    public void addGUITask(GUITask guiTask){
        guiQueue.add(guiTask);
    }

    private static RenderQueue renderQueue;

    public void clear() {
        queue.clear();
        guiQueue.clear();
    }

    public Iterable<RenderTask> getTasks() {
        return Collections.unmodifiableCollection(queue);
    }

    public Iterable<GUITask> getGUITasks() {
        return Collections.unmodifiableCollection(guiQueue);
    }

    public static RenderQueue getDefault(){
        if(renderQueue == null){
            renderQueue = new RenderQueue();
        }
        return renderQueue;
    }
}
