package test;

import model.Task;
import model.TaskManager;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class TaskManagerTest {

    @Test
    public void addTaskTest() {

        TaskManager manager = new TaskManager();

        Task task = new Task(
                "Task",
                "Description",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );

        manager.addTask(task);

        assertEquals(1, manager.getTaskCount());
        assertTrue(manager.getTasks().contains(task));
    }

    @Test
    public void removeTaskTest() {

        TaskManager manager = new TaskManager();

        Task task = new Task(
                "Task",
                "Description",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );

        manager.addTask(task);
        manager.removeTask(task);

        assertEquals(0, manager.getTaskCount());
        assertFalse(manager.getTasks().contains(task));
    }

    @Test
    public void clearTasksTest() {

        TaskManager manager = new TaskManager();

        manager.addTask(new Task(
                "1",
                "",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        ));

        manager.addTask(new Task(
                "2",
                "",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        ));

        manager.clearTasks();

        assertEquals(0, manager.getTaskCount());
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    public void getTaskCountTest() {

        TaskManager manager = new TaskManager();

        manager.addTask(new Task(
                "Task",
                "",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        ));

        manager.addTask(new Task(
                "Task2",
                "",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)
        ));

        assertEquals(2, manager.getTaskCount());
    }

}