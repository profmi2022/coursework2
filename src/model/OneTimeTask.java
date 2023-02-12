package model;

import exception.IncorrectArgumentException;

import java.time.LocalDateTime;

/**
 * Однократная задача
 */

public class OneTimeTask extends Task {

    public OneTimeTask(String title, String description, TaskType taskType, LocalDateTime dateTime) throws IncorrectArgumentException {
        super(title, description, taskType, dateTime);
    }

    @Override
    public LocalDateTime getNextDateTime(LocalDateTime dateTime) {
                return null;
    }
}
