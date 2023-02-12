package model;

import exception.IncorrectArgumentException;

import java.time.LocalDateTime;

/**
 * Ежегодная задача
 */

public class YearlyTask extends Task {

    public YearlyTask (String title, String description, TaskType taskType, LocalDateTime dateTime) throws IncorrectArgumentException {
        super(title, description, taskType, dateTime);
    }

    @Override
    public LocalDateTime getNextDateTime(LocalDateTime dateTime) {
        return dateTime.plusYears(1);
    }
}