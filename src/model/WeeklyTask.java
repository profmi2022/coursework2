package model;

import exception.IncorrectArgumentException;

import java.time.LocalDateTime;

/**
 * Еженедельная задача
 */

public class WeeklyTask extends Task {

    public WeeklyTask (String title, String description, TaskType taskType, LocalDateTime dateTime) throws IncorrectArgumentException {
        super(title, description, taskType, dateTime);
    }

    @Override
    public LocalDateTime getNextDateTime(LocalDateTime dateTime) {
        return dateTime.plusWeeks(1);
    }
}