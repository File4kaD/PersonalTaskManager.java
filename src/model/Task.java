package model;

import java.time.LocalDateTime;

public class Task {

    // Название задачи
    private String title;

    // Описание задачи
    private String description;

    // Дата и время начала
    private LocalDateTime startDateTime;

    // Дата и время окончания
    private LocalDateTime endDateTime;

    // Конструктор
    public Task(String title, String description,
                LocalDateTime startDateTime,
                LocalDateTime endDateTime) {

        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    // геттер

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    // сеттер

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {

        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {

        this.endDateTime = endDateTime;
    }



    @Override
    public String toString() {
        return title;
    }
}