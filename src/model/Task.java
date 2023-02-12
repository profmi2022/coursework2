package model;

import exception.IncorrectArgumentException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public abstract class Task {

    private static int idGenerator = 1;

    private String title;
    private TaskType taskType;
    private int id;
    private LocalDateTime taskTime;
    private String description;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public Task(String title,  String description, TaskType taskType, LocalDateTime taskTime) throws IncorrectArgumentException {
        setTitle(title);
        setTaskType(taskType);
        this.id = idGenerator++;
        setTaskTime(taskTime);
        setDescription(description);
    }

    @Override
    public String toString() {

        return "Задача: " + title +
                ", тип : " + taskType.getName() +
                ", id " + id +
                ", дата: " + taskTime.format(DATE_FORMATTER) +
                ", описание: " + description;
    }

    public abstract LocalDateTime getNextDateTime(LocalDateTime dateTime);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws IncorrectArgumentException {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        } else {
            throw new IncorrectArgumentException("Заголовок задачи");
        }
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) throws IncorrectArgumentException {
        if (taskType != null) {
            this.taskType = taskType;
        } else {
            throw new IncorrectArgumentException("Тип задачи");
        }
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(LocalDateTime taskTime) throws IncorrectArgumentException {
        if (taskTime != null) {
            this.taskTime = taskTime;
        } else {
            throw new IncorrectArgumentException("Дата и время задачи");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws IncorrectArgumentException {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        } else {
            throw new IncorrectArgumentException("Описание задачи");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && taskType == task.taskType && Objects.equals(taskTime, task.taskTime) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, taskType, id, taskTime, description);
    }
}

