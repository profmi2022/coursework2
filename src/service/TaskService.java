package service;

import exception.IncorrectArgumentException;
import exception.TaskNotFoundException;
import model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Управление задачами
 */
public class TaskService {

    private final Map<Integer, Task> taskMap = new HashMap<>();

    private final Collection<Task> removedTasks = new ArrayList<>();

    private final Map<LocalDate, Collection<Task>> groupedTasks = new TreeMap<>();

    public void add(Task task) throws IncorrectArgumentException {
        if (task != null) {
            this.taskMap.put(task.getId(), task);
        } else {
            throw new IncorrectArgumentException("Задача");
        }
    }

    public Task remove(Integer taskId) throws TaskNotFoundException {
        Task task = null;
        if (this.taskMap.containsKey(taskId)) {
            task = this.taskMap.get(taskId);
            this.taskMap.remove(taskId);
            this.removedTasks.add(task);
        } else {
            throw new TaskNotFoundException(taskId);
        }
        return task;
    }

    public void printGroupedTasks() {
        for (Map.Entry<Integer, Task> taskEntry : taskMap.entrySet()) {
            Task task = taskEntry.getValue();
            LocalDate date = task.getTaskTime().toLocalDate();
            if (groupedTasks.containsKey(date)) {   //Проверка даты текущей задачи в коллекции
                Collection<Task> tasksList = groupedTasks.get(date);    //Если эта дата есть, возвращает список по текущей дате
                if (tasksList == null) {                                //Если списка нет, создаем его
                    tasksList = new ArrayList<>();
                }
                tasksList.add(task);
                groupedTasks.replace(date, tasksList);                  //Добавляем задачу в список
            } else {                                                    //если нет такой даты
                Collection<Task> tasksList = new ArrayList<>();         //создаем список
                tasksList.add(task);                                    //добавляем в него задачу
                groupedTasks.put(date, tasksList);                      //помещаем в Map groupedTasks дату и список задач на эту дату
            }
        }

//Вывод сгруппированных по датам задач на печать
        for (Map.Entry<LocalDate, Collection<Task>> localDateCollectionEntry : groupedTasks.entrySet()) {
            System.out.println("Дата: " + localDateCollectionEntry.getKey());
            for (Task task : localDateCollectionEntry.getValue()) {
                System.out.println("   " + task);
            }
        }
    }


    public Collection<Task> getAllByDate(LocalDate date) {
        Collection<Task> tasksByDay = new ArrayList<>();
        Collection<Task> allTasks = taskMap.values();

        for (Task task : allTasks) {
            LocalDateTime currentDateTime = task.getTaskTime();

            if (currentDateTime.toLocalDate().equals(date)) {
                tasksByDay.add(task);
                break;
            }

            LocalDateTime taskNextTime = currentDateTime;

            do {
                taskNextTime = task.getNextDateTime(taskNextTime);

                if (taskNextTime == null) {
                    break;
                }
                if (taskNextTime.toLocalDate().equals(date)) {
                    tasksByDay.add(task);
                    break;
                }

            } while (taskNextTime.toLocalDate().isBefore(date));
        }
        return tasksByDay;


    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public Collection<Task> getRemovedTasks() {
        return removedTasks;
    }
}

