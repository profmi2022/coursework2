import exception.IncorrectArgumentException;
import exception.TaskNotFoundException;
import model.*;
import service.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    private static final TaskService taskService = new TaskService();
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}\\:\\d{2}");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void main(String[] args) throws IncorrectArgumentException{
        createTask("Задача 1","Описание задачи 1", TaskType.PERSONAL, LocalDateTime.now(), 2);
        createTask("Задача 2","Описание задачи 2", TaskType.WORK, LocalDateTime.now(), 3);
        createTask("Задача 3","Описание задачи 3", TaskType.PERSONAL, LocalDateTime.now(), 5);
        createTask("Задача 4","Описание задачи 4", TaskType.PERSONAL, LocalDateTime.of(2023, 2,15, 0,0,0,0), 1);
        createTask("Задача 5","Описание задачи 5", TaskType.WORK, LocalDateTime.of(2023, 2,17, 0,0,0,0), 2);
        createTask("Задача 6","Описание задачи 6", TaskType.PERSONAL, LocalDateTime.of(2023, 2,17, 0,0,0,0), 3);
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                printMenu();
                System.out.print("Выберите пункт меню: ");
                if (scanner.hasNextInt()) {
                    int menu = scanner.nextInt();
                    switch (menu) {
                        case 1:
                            inputTask(scanner);
                            break;
                        case 2:
                            removeTask(scanner);
                            break;
                        case 3:
                            printTasksByDate(scanner);
                            break;
                        case 4:
                            printMapTask();
                            break;
                        case 5:
                            System.out.println("Список архивных задач:");
                            for (Task removedTask : taskService.getRemovedTasks()) {
                                System.out.println(removedTask);
                            }
                            break;
                        case 6:
                            editTask(scanner);
                            break;
                        case 7:
                            taskService.printGroupedTasks();
                            break;
                        case 0:
                            break label;
                    }
                } else {
                    scanner.next();
                    System.out.println("Выберите пункт меню из списка!");
                }
            }
        }
    }

    private static void inputTask(Scanner scanner) {
        scanner.useDelimiter("\n");

        String title = inputTaskTitle(scanner);
        String description = inputTaskDescription(scanner);
        TaskType type= inputTaskType(scanner);
        LocalDateTime taskTime = inputLocalDateTime(scanner);
        int repeatability = inputRepeatability(scanner);

        createTask(title, description, type, taskTime, repeatability);

    }

    private static void editTask(Scanner scanner) throws IncorrectArgumentException {
        scanner.useDelimiter("\n");

        System.out.println("Введите id задачи для редактирования");
        int id = scanner.nextInt();

        String title = inputTaskTitle(scanner);
        String description = inputTaskDescription(scanner);
        Task task = taskService.getTaskMap().get(id);
        try {
            task.setTitle(title);
            task.setDescription(description);
        } catch (IncorrectArgumentException e) {
            e.printStackTrace();
        }
    }

    private static void createTask(String title, String description, TaskType type, LocalDateTime taskTime, int repeatability) {
        Task task = null;

        try {
            switch (repeatability) {
                case 1:
                    task = new OneTimeTask(title, description, type, taskTime);
                    break;
                case 2:
                    task = new DailyTask(title, description, type, taskTime);
                    break;
                case 3:
                    task = new WeeklyTask(title, description, type, taskTime);
                    break;
                case 4:
                    task = new MonthlyTask(title, description, type, taskTime);
                    break;
                case 5:
                    task = new YearlyTask(title, description, type, taskTime);
                    break;
                default:
                    System.out.println("Повторяемость задачи введена некорректно");
            }
            if (task != null) {
                taskService.add(task);
                System.out.println("Задача добавлена");
            } else {
                System.out.println("Введены некорректные данные по задаче");
            }
        } catch (IncorrectArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    private static String inputTaskTitle(Scanner scanner) {
        System.out.print("Введите название задачи: ");
        String title = scanner.next();

        if (title.isBlank()) {
            System.out.println("Необходимо ввести название задачи!");
            scanner.close();
        }
        return title;
    }

    private static String inputTaskDescription(Scanner scanner) {
        System.out.print("Введите описание задачи: ");
        String description = scanner.next();

        if (description.isBlank()) {
            System.out.println("Необходимо ввести описание задачи!");
            scanner.close();
        }
        return description;
    }

    private static TaskType inputTaskType(Scanner scanner) {
        System.out.print("Введите тип задачи (1 - личная, 2 - рабочая): ");
        TaskType type = null;

        int taskTypeChoice = scanner.nextInt();

        if (taskTypeChoice == 1) {
            type = TaskType.PERSONAL;
        } else if (taskTypeChoice == 2) {
            type = TaskType.WORK;
        } else {
            System.out.println("Тип задачи введен некорректно");
            scanner.close();
        }
        return type;
    }

    private static LocalDateTime inputLocalDateTime(Scanner scanner){
        System.out.println("Введите дату и время задачи в формате dd.MM.yyy HH:mm");

        LocalDateTime taskTime = null;
        if (scanner.hasNext(DATE_TIME_PATTERN)) {
            String dateTime = scanner.next(DATE_TIME_PATTERN);
            return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
        } else {
            System.out.println("Введите дату и время задачи в формате dd.MM.yyy HH:mm");
            scanner.close();
            return null;
        }
    }

    private static int inputRepeatability(Scanner scanner) {
        System.out.println("Введите повторяемость задачи (1 - однократно, 2 - каждый день," +
                " 3 - каждую неделю, 4 - каждый месяц, 5 - каждый год");
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        } else {
            System.out.println("Введите числом повторяемость задачи");
            scanner.close();
        }
        return -1;
    }

    private static void removeTask(Scanner scanner){
        System.out.println("Введите id задачи для удаления");
        int id = scanner.nextInt();

        try {
            taskService.remove(id);
        }catch (TaskNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    private static void printTasksByDate(Scanner scanner){
        System.out.println("Введите дату в формате dd.MM.yyy");

          if (scanner.hasNext(DATE_PATTERN)) {
            String taskTime = scanner.next(DATE_PATTERN);
            LocalDate inputDate = LocalDate.parse(taskTime, DATE_FORMATTER);
              System.out.println(inputDate);
              Collection<Task> tasksByDate = taskService.getAllByDate(inputDate);

              for (Task task : tasksByDate) {
                  System.out.println(task);
              }
        } else {
            System.out.println("Введите дату в формате dd.MM.yyy");
            scanner.close();
        }
    }

    private static void printMenu() {
        System.out.println("1. Добавить задачу\n" +
                "2. Удалить задачу\n" +
                "3. Получить задачи на указанный день\n" +
                "4. Вывод всех задач\n" +
                "5. Список всех удаленных задач\n" +
                "6. Редактирование заголовка и описания задачи\n" +
                "7. Вывод всех задач, сгруппированных по датам\n" +
                "0. Выход");
    }

    // Вывод всех задач в консоль
    public static void printMapTask(){
        for (Map.Entry<Integer, Task> taskEntry : taskService.getTaskMap().entrySet()) {
            String s = "";
            if(taskEntry.getValue() instanceof OneTimeTask){
                s = "одноразовая";
            }
            if(taskEntry.getValue() instanceof DailyTask){
                s = "ежедневная";
            }
            if(taskEntry.getValue() instanceof WeeklyTask){
                s = "еженедельная";
            }
            if(taskEntry.getValue() instanceof MonthlyTask){
                s = "ежемесячная";
            }
            if(taskEntry.getValue() instanceof YearlyTask){
                s = "ежегодная";
            }
            System.out.println("id задачи - " + taskEntry.getValue().getId() + ", "
                    + taskEntry.getValue().getTitle() + ", "
                    + taskEntry.getValue().getDescription() + ", "
                    + taskEntry.getValue().getTaskType().getName() + ", "
                    + taskEntry.getValue().getTaskTime() + ", "
                    + "периодичность " + s);
        }
    }
}
