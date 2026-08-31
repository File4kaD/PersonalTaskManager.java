package view;

import model.Task;

import java.awt.Rectangle;

public class TaskView {

    private Task task;
    private Rectangle bounds;

    public TaskView(Task task, Rectangle bounds) {
        this.task = task;
        this.bounds = bounds;
    }

    public Task getTask() {
        return task;
    }

    public Rectangle getBounds() {
        return bounds;
    }

}