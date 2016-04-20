package dat367.falling.platform_abstraction;

import dat367.falling.math.Vector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class RenderQueue {

    private static Queue<RenderTask> queue = new LinkedList<RenderTask>();

    public static void addTask(RenderTask task) {
        queue.add(task);
    }

    public static RenderTask getNextTask() {
        return queue.remove();
    }

    public static boolean hasMoreTasks() {
        return !queue.isEmpty();
    }

    public static void clear() {
        queue.clear();
    }

    public static Iterable<RenderTask> getTasks() {
        return Collections.unmodifiableCollection(queue);
    }

    public static class RenderTask {
        public Model model;
        public Vector position;
        public Vector orientation;
        public Vector scale;

        public RenderTask(Model model, Vector position, Vector orientation, Vector scale) {
            this.model = model;
            this.position = position;
            this.orientation = orientation;
            this.scale = scale;
        }
    }

}
