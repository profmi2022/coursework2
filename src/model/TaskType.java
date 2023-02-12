package model;

public enum TaskType {
    PERSONAL("Личная задача"),
    WORK("Рабочая задача");

    private String name;

    TaskType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

