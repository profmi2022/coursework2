package model;

import exception.IncorrectArgumentException;

import java.time.LocalDateTime;

/**
 * Ежедневная задача
 */

public class DailyTask extends Task {

    public DailyTask (String title, String description, TaskType taskType, LocalDateTime dateTime) throws IncorrectArgumentException {
        super(title, description, taskType, dateTime);
    }

    @Override
    public LocalDateTime getNextDateTime(LocalDateTime dateTime) {
        return dateTime.plusDays(1);
    }
}
