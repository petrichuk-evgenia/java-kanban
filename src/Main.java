import ru.practikum.manager.TaskManager;
import ru.practikum.task.Epic;
import ru.practikum.task.Status;
import ru.practikum.task.Subtask;
import ru.practikum.task.Task;
import ru.practikum.utils.Managers;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import static ru.practikum.manager.InMemoryTaskManager.printIssue;

public class Main {

    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        try {
            test(inMemoryTaskManager);
            testHistory(inMemoryTaskManager);
            //handMenuChoice(inMemoryTaskManager);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод демонстрации основных функций TaskManager.
     * Выполняет следующие действия:
     * 1. Добавляет задачи, эпики и подзадачи.
     * 2. Обновляет статусы задач и эпиков.
     * 3. Обновляет подзадачи.
     * 4. Выводит информацию о каждой отдельной задаче.
     * 5. Удаляет некоторые задачи.
     * 6. Очищает списки задач (Task, Subtask, Epic).
     * 7. Повторно добавляет эпики с подзадачами и снова очищает список Epic.
     * 8. Демонстрирует историю просмотров задач
     *
     * @param taskManager экземпляр класса TaskManager, используемый для выполнения операций
     */
    private static void test(TaskManager taskManager) {
        System.out.println("Демо:");
        System.out.println("\nДобавление задач:");
        int t1Id = taskManager.addIssue(new Task("task1", "task1_desc"));
        int t2Id = taskManager.addIssue(new Task("task2", "task2_desc"));
        int e1Id = taskManager.addIssue(new Epic("epic1", "epic1_desc"));
        int e2Id = taskManager.addIssue(new Epic("epic2", "epic2_desc"));
        int s1Id = taskManager.addIssue(new Subtask("subtask1", "subtask1_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s2Id = taskManager.addIssue(new Subtask("subtask2", "subtask2_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s3Id = taskManager.addIssue(new Subtask("subtask3", "subtask3_desc", taskManager.getIssueById(e2Id, "Epic").getId()));
        int s4Id = taskManager.addIssue(new Subtask("subtask4", "subtask4_desc", taskManager.getIssueById(e2Id, "Epic").getId()));
        taskManager.printAllIssues();
        System.out.println("\nИзменение тасков и эпиков:");
        taskManager.updateIssue(t1Id, new Task("task1_upd", "task1_desc_upd", Status.IN_PROGRESS));
        taskManager.updateIssue(t2Id, new Task("task2_upd", "task2_desc_upd", Status.DONE));
        taskManager.updateIssue(e2Id, new Epic("epic2_upd", "epic2_desc_upd"));
        System.out.println("\nИзменение сабтасков:");
        taskManager.updateIssue(s1Id, new Subtask("subtask1_upd", "subtask1_desc_upd", Status.IN_PROGRESS));
        taskManager.updateIssue(s2Id, new Subtask("subtask2_upd", "subtask2_desc_upd", Status.DONE));
        taskManager.updateIssue(s3Id, new Subtask("subtask3_upd", "subtask3_desc_upd", Status.DONE));
        taskManager.updateIssue(s4Id, new Subtask("subtask4_upd", "subtask4_desc_upd", Status.DONE));
        taskManager.printAllIssues();
        System.out.println("\nПросмотр каждой отдельной задачи:");
        System.out.println(taskManager.getIssueById(t1Id, "Task").toString());
        System.out.println(taskManager.getIssueById(t2Id, "Task").toString());
        System.out.println(taskManager.getIssueById(e1Id, "Epic").toString());
        System.out.println(taskManager.getIssueById(e2Id, "Epic").toString());
        System.out.println(taskManager.getIssueById(s1Id, "Subtask").toString());
        System.out.println(taskManager.getIssueById(s2Id, "Subtask").toString());
        System.out.println(taskManager.getIssueById(s3Id, "Subtask").toString());
        System.out.println(taskManager.getIssueById(s4Id, "Subtask").toString());
        System.out.println("\nУдаление:" + String.format(" Task %d, Subtask %d, Epic %d", t2Id, s1Id, e2Id));
        ;
        taskManager.removeIssueById(t2Id, "Task");
        taskManager.removeIssueById(s1Id, "Subtask");
        taskManager.removeIssueById(e2Id, "Epic");
        taskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Task");
        taskManager.clearIssuesList("Task");
        taskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Subtask");
        taskManager.clearIssuesList("Subtask");
        taskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Epic без сабтасков");
        taskManager.clearIssuesList("Epic");
        taskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Epic c сабтасками");
        int e1IdNew = taskManager.addIssue(new Epic("epic1", "epic1_desc"));
        int e2IdNew = taskManager.addIssue(new Epic("epic2", "epic2_desc"));
        taskManager.addIssue(new Subtask("subtask1", "subtask1_desc", taskManager.getIssueById(e1IdNew, "Epic").getId()));
        taskManager.addIssue(new Subtask("subtask2", "subtask2_desc", taskManager.getIssueById(e1IdNew, "Epic").getId()));
        taskManager.addIssue(new Subtask("subtask3", "subtask3_desc", taskManager.getIssueById(e2IdNew, "Epic").getId()));
        taskManager.addIssue(new Subtask("subtask4", "subtask4_desc", taskManager.getIssueById(e2IdNew, "Epic").getId()));
        System.out.println("\nДо очистки:");
        taskManager.printAllIssues();
        taskManager.clearIssuesList("Epic");
        System.out.println("\nПосле очистки:");
        taskManager.printAllIssues();
    }

    private static void testHistory(TaskManager taskManager) {
        System.out.println("Демо: История просмотров задач");
        System.out.println("История просмотров задач до начала:");
        printHistory(taskManager.getHistory());
        System.out.println("\nДобавление задач:");
        int t1Id = taskManager.addIssue(new Task("task1", "task1_desc"));
        int e1Id = taskManager.addIssue(new Epic("epic1", "epic1_desc"));
        int s1Id = taskManager.addIssue(new Subtask("subtask1", "subtask1_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s2Id = taskManager.addIssue(new Subtask("subtask2", "subtask2_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        taskManager.printAllIssues();
        System.out.println("\nИстория просмотров задач после добавления:");
        printHistory(taskManager.getHistory());
        System.out.println("\nПросмотр таски1 и сабтаски1 отдельно:");
        System.out.println(taskManager.getIssueById(t1Id, "Task").toString());
        System.out.println(taskManager.getIssueById(s1Id, "Subtask").toString());
        System.out.println("\nИстория просмотров задач после просмотра таски и сабтаски:");
        printHistory(taskManager.getHistory());
        System.out.println("\nИзменение сабтаски2 и эпика1:");
        taskManager.updateIssue(e1Id, new Epic("epic1_upd", "epic1_desc_upd"));
        taskManager.updateIssue(s2Id, new Subtask("subtask2_upd", "subtask2_desc_upd", Status.IN_PROGRESS));
        taskManager.printAllIssues();
        System.out.println("\nИстория просмотров задач после изменения:");
        printHistory(taskManager.getHistory());
        System.out.println("\nИстория просмотров задач после удаления сабтаски1:");
        taskManager.removeIssueById(s1Id, "Subtask");
        printHistory(taskManager.getHistory());
        System.out.println("\nИстория просмотров задач после очистки эпиков:");
        taskManager.clearIssuesList("Epic");
        printHistory(taskManager.getHistory());
    }

    private static void handMenuChoice(TaskManager taskManager) {
        while (true) {
            printMenu();
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1 -> addIssue(taskManager);
                    case 2 -> printIssueByManager(taskManager);
                    case 3 -> updateIssue(taskManager);
                    case 4 -> removeIssue(taskManager);
                    case 5 -> taskManager.printIssues(chooseIssueType());
                    case 6 -> taskManager.printAllIssues();
                    case 7 -> taskManager.clearIssuesList(chooseIssueType());
                    case 8 -> printHistory(taskManager.getHistory());
                    case 9 -> {
                        System.out.println("Работа завершена");
                        return;
                    }
                    default -> {
                        System.out.println("Такой команды нет");
                        break;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Вы ввели бурду вместо корректной команды");
                scanner.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void printMenu() {
        System.out.println("\nЧто вы хотите сделать?");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Посмотреть задачу");
        System.out.println("3. Изменить задачу");
        System.out.println("4. Удалить задачу");
        System.out.println("5. Посмотреть список задач по выбранному типу");
        System.out.println("6. Посмотреть список задач всех типов");
        System.out.println("7. Очистить список задач");
        System.out.println("8. История просмотров задач");
        System.out.println("9. Выход");
    }

    private static String chooseIssueType() {
        System.out.println("Выберите тип задачи:");
        System.out.println("1. Тask");
        System.out.println("2. Subtask");
        System.out.println("3. Epic");
        int choice;
        while (true) {
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> {
                    return "Task";
                }
                case 2 -> {
                    return "Subtask";
                }
                case 3 -> {
                    return "Epic";
                }
                default -> {
                    System.out.println("Выберите корректный тип задач");
                    break;
                }
            }
        }
    }

    private static Status chooseStatusType() {
        System.out.println("Выберите статус задачи:");
        System.out.println("1. In progress");
        System.out.println("2. Done");
        int choice;
        while (true) {
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> {
                    return Status.IN_PROGRESS;
                }
                case 2 -> {
                    return Status.DONE;
                }
                default -> {
                    System.out.println("Выберите корректный тип статуса задачи");
                    break;
                }
            }
        }
    }

    private static void addIssue(TaskManager taskManager) {
        String type = chooseIssueType();
        System.out.println("Введите название задачи:");
        String summary;
        summary = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description;
        description = scanner.nextLine();
        int id = 0;
        switch (type) {
            case "Task" -> {
                id = taskManager.addIssue(new Task(summary, description));
            }
            case "Epic" -> {
                id = taskManager.addIssue(new Epic(summary, description));
            }
            case "Subtask" -> {
                if (taskManager.getIssuesList("Epic", Epic.class).size() == 0) {
                    System.out.println("Нет эпиков. Добавьте эпик");
                    return;
                }
                int epicId;
                System.out.println("Введите ID родительского эпика:");
                epicId = scanner.nextInt();
                scanner.nextLine();
                id = taskManager.addIssue(new Subtask(summary, description, taskManager.getIssueById(epicId, "Epic").getId()));
            }
        }
        System.out.println("Задача добавлена. ID: " + id);
    }

    private static void printIssueByManager(TaskManager taskManager) {
        String type = chooseIssueType();
        System.out.println("Введите ID задачи, которую хотите посмотреть:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println(taskManager.getIssueById(id, type).toString());
    }

    private static void updateIssue(TaskManager taskManager) {
        String type = chooseIssueType();
        System.out.println("Введите ID задачи, которую хотите изменить:");
        int id = scanner.nextInt();
        scanner.nextLine();
        try {
            //проверка на существование задачи
            taskManager.getIssueById(id, type);
            System.out.println("Введите новое название задачи:");
            String summary = scanner.nextLine();
            System.out.println("Введите новое описание задачи:");
            String description = scanner.nextLine();
            Status newStatus = chooseStatusType();

            switch (type) {
                case "Task" -> taskManager.updateIssue(id, new Task(summary, description, newStatus));
                case "Epic" -> taskManager.updateIssue(id, new Epic(summary, description));
                case "Subtask" -> taskManager.updateIssue(id, new Subtask(summary, description, newStatus));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeIssue(TaskManager taskManager) {
        String type = chooseIssueType();
        System.out.println("Введите ID задачи, которую хотите удалить:");
        int id = scanner.nextInt();
        scanner.nextLine();
        try {
            taskManager.removeIssueById(id, type);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static <T extends Task> void printHistory(Map<String, T> history) {
        history.values().forEach(issue -> {
            printIssue(issue);
        });
    }
}
