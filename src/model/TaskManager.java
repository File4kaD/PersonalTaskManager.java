package model;

import java.util.ArrayList;
import java.util.List;

// Класс для хранения и управления задачами
public class TaskManager {

    // Список всех задач
    private final List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    // Добавляет задачу
    public void addTask(Task task) {
        tasks.add(task);
    }

    // Удаляет задачу

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    // Очищает список задач

    public void clearTasks() {
        tasks.clear();
    }

    // Загружает список задач
    public void setTasks(List<Task> tasks) {

        this.tasks.clear();

        this.tasks.addAll(tasks);

    }

    // Возвращает список всех задач
    public List<Task> getTasks() {
        return tasks;
    }

    // Возвращает количество задач
    public int getTaskCount() {
        return tasks.size();
    }
}