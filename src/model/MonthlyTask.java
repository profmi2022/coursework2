package model;

import exception.IncorrectArgumentException;

import java.time.LocalDateTime;

/**
 * Ежемесячная задача
 */

public class MonthlyTask extends Task {

    public MonthlyTask (String title, String description, TaskType taskType, LocalDateTime dateTime) throws IncorrectArgumentException {
        super(title, description, taskType, dateTime);
    }

    @Override
    public LocalDateTime getNextDateTime(LocalDateTime dateTime) {
                return dateTime.plusMonths(1);
    }
}