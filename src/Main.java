import ru.practikum.Epic;
import ru.practikum.Subtask;
import ru.practikum.Task;
import ru.practikum.TaskManager;
import ru.practikum.constants.IssueTypes;
import ru.practikum.constants.Status;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        try {
            //test(taskManager);
            handMenuChoice(taskManager);
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
     *
     * @param taskManager экземпляр класса TaskManager, используемый для выполнения операций
     */
    private static void test(TaskManager taskManager) {
        System.out.println("Демо:");
        System.out.println("\nДобавление задач:");
        int t1Id = TaskManager.addIssue(new Task("task1", "task1_desc", taskManager.incTaskIdCounter()));
        int t2Id = TaskManager.addIssue(new Task("task2", "task2_desc", taskManager.incTaskIdCounter()));
        int e1Id = TaskManager.addIssue(new Epic("epic1", "epic1_desc", taskManager.incEpicIdCounter()));
        int e2Id = TaskManager.addIssue(new Epic("epic2", "epic2_desc", taskManager.incEpicIdCounter()));
        int s1Id = TaskManager.addIssue(new Subtask("subtask1", "subtask1_desc", TaskManager.getIssueById(e1Id, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
        int s2Id = TaskManager.addIssue(new Subtask("subtask2", "subtask2_desc", TaskManager.getIssueById(e1Id, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
        int s3Id = TaskManager.addIssue(new Subtask("subtask3", "subtask3_desc", TaskManager.getIssueById(e2Id, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
        int s4Id = TaskManager.addIssue(new Subtask("subtask4", "subtask4_desc", TaskManager.getIssueById(e2Id, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
        TaskManager.printAllIssues();
        System.out.println("\nИзменение тасков и эпиков:");
        TaskManager.updateIssue(t1Id, new Task("task1_upd", "task1_desc_upd", Status.IN_PROGRESS, taskManager.incTaskIdCounter()));
        TaskManager.updateIssue(t2Id, new Task("task2_upd", "task2_desc_upd", Status.DONE, taskManager.incTaskIdCounter()));
        TaskManager.updateIssue(e2Id, new Epic("epic2_upd", "epic2_desc_upd", Status.DONE, taskManager.incEpicIdCounter()));
        System.out.println("\nИзменение сабтасков:");
        TaskManager.updateIssue(s1Id, new Subtask("subtask1_upd", "subtask1_desc_upd", Status.IN_PROGRESS, taskManager.incSubtaskIdCounter()));
        TaskManager.updateIssue(s2Id, new Subtask("subtask2_upd", "subtask2_desc_upd", Status.DONE, taskManager.incSubtaskIdCounter()));
        TaskManager.updateIssue(s3Id, new Subtask("subtask3_upd", "subtask3_desc_upd", Status.DONE, taskManager.incSubtaskIdCounter()));
        TaskManager.updateIssue(s4Id, new Subtask("subtask4_upd", "subtask4_desc_upd", Status.DONE, taskManager.incSubtaskIdCounter()));
        TaskManager.printAllIssues();
        System.out.println("\nПросмотр каждой отдельной задачи:");
        System.out.println(TaskManager.getIssueById(t1Id, IssueTypes.TASK).toString());
        System.out.println(TaskManager.getIssueById(t2Id, IssueTypes.TASK).toString());
        System.out.println(TaskManager.getIssueById(e1Id, IssueTypes.EPIC).toString());
        System.out.println(TaskManager.getIssueById(e2Id, IssueTypes.EPIC).toString());
        System.out.println(TaskManager.getIssueById(s1Id, IssueTypes.SUBTASK).toString());
        System.out.println(TaskManager.getIssueById(s2Id, IssueTypes.SUBTASK).toString());
        System.out.println(TaskManager.getIssueById(s3Id, IssueTypes.SUBTASK).toString());
        System.out.println(TaskManager.getIssueById(s4Id, IssueTypes.SUBTASK).toString());
        System.out.println("\nУдаление:");
        TaskManager.removeIssueById(t2Id, IssueTypes.TASK);
        TaskManager.removeIssueById(s1Id, IssueTypes.SUBTASK);
        TaskManager.removeIssueById(e2Id, IssueTypes.EPIC);
        TaskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Task");
        TaskManager.clearIssuesList(IssueTypes.TASK);
        TaskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Subtask");
        TaskManager.clearIssuesList(IssueTypes.SUBTASK);
        TaskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Epic без сабтасков");
        TaskManager.clearIssuesList(IssueTypes.EPIC);
        TaskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Epic c сабтасками");
        int e1Id_new = TaskManager.addIssue(new Epic("epic1", "epic1_desc", taskManager.incEpicIdCounter()));
        int e2Id_new = TaskManager.addIssue(new Epic("epic2", "epic2_desc", taskManager.incEpicIdCounter()));
        TaskManager.addIssue(new Subtask("subtask1", "subtask1_desc", TaskManager.getIssueById(e1Id_new, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
        TaskManager.addIssue(new Subtask("subtask2", "subtask2_desc", TaskManager.getIssueById(e1Id_new, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
        TaskManager.addIssue(new Subtask("subtask3", "subtask3_desc", TaskManager.getIssueById(e2Id_new, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
        TaskManager.addIssue(new Subtask("subtask4", "subtask4_desc", TaskManager.getIssueById(e2Id_new, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
        System.out.println("\nДо очистки:");
        TaskManager.printAllIssues();
        TaskManager.clearIssuesList(IssueTypes.EPIC);
        System.out.println("\nПосле очистки:");
        TaskManager.printAllIssues();
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
                    case 2 -> printIssue();
                    case 3 -> updateIssue(taskManager);
                    case 4 -> removeIssue();
                    case 5 -> TaskManager.printIssues(chooseIssueType());
                    case 6 -> TaskManager.printAllIssues();
                    case 7 -> TaskManager.clearIssuesList(chooseIssueType());
                    case 8 -> {
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
        System.out.println("8. Выход");
    }

    private static IssueTypes chooseIssueType() {
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
                    return IssueTypes.TASK;
                }
                case 2 -> {
                    return IssueTypes.SUBTASK;
                }
                case 3 -> {
                    return IssueTypes.EPIC;
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
        IssueTypes type = chooseIssueType();
        System.out.println("Введите название задачи:");
        String summary;
        summary = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description;
        description = scanner.nextLine();
        int id = 0;
        switch (type) {
            case TASK -> {
                id = TaskManager.addIssue(new Task(summary, description, taskManager.incTaskIdCounter()));
            }
            case EPIC -> {
                id = TaskManager.addIssue(new Epic(summary, description, taskManager.incEpicIdCounter()));
            }
            case SUBTASK -> {
                if (TaskManager.getIssuesList(IssueTypes.EPIC, Epic.class).size() == 0) {
                    System.out.println("Нет эпиков. Добавьте эпик");
                    return;
                }
                int epicId;
                System.out.println("Введите ID родительского эпика:");
                epicId = scanner.nextInt();
                scanner.nextLine();
                id = TaskManager.addIssue(new Subtask(summary, description, TaskManager.getIssueById(epicId, IssueTypes.EPIC).getId(), taskManager.incSubtaskIdCounter()));
            }
        }
        System.out.println("Задача добавлена. ID: " + id);
    }

    private static void printIssue() {
        IssueTypes type = chooseIssueType();
        System.out.println("Введите ID задачи, которую хотите посмотреть:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println(TaskManager.getIssueById(id, type).toString());
    }

    private static void updateIssue(TaskManager taskManager) {
        IssueTypes type = chooseIssueType();
        System.out.println("Введите ID задачи, которую хотите изменить:");
        int id = scanner.nextInt();
        scanner.nextLine();
        try {
            //проверка на существование задачи
            TaskManager.getIssueById(id, type);
            System.out.println("Введите новое название задачи:");
            String summary = scanner.nextLine();
            System.out.println("Введите новое описание задачи:");
            String description = scanner.nextLine();
            Status newStatus = chooseStatusType();

            switch (type) {
                case TASK -> TaskManager.updateIssue(id, new Task(summary, description, newStatus, taskManager.incTaskIdCounter()));
                case EPIC -> TaskManager.updateIssue(id, new Epic(summary, description, newStatus, taskManager.incEpicIdCounter()));
                case SUBTASK -> TaskManager.updateIssue(id, new Subtask(summary, description, newStatus, taskManager.incSubtaskIdCounter()));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeIssue() {
        IssueTypes type = chooseIssueType();
        System.out.println("Введите ID задачи, которую хотите удалить:");
        int id = scanner.nextInt();
        scanner.nextLine();
        try {
            TaskManager.removeIssueById(id, type);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
