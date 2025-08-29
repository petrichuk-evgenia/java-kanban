import ru.practikum.task.Epic;
import ru.practikum.task.Subtask;
import ru.practikum.task.Task;
import ru.practikum.manager.TaskManager;
import ru.practikum.task.IssueTypes;
import ru.practikum.task.Status;

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
        int t1Id = taskManager.addIssue(new Task(taskManager.incTaskIdCounter(), "task1", "task1_desc"));
        int t2Id = taskManager.addIssue(new Task(taskManager.incTaskIdCounter(), "task2", "task2_desc"));
        int e1Id = taskManager.addIssue(new Epic(taskManager.incEpicIdCounter(),"epic1", "epic1_desc"));
        int e2Id = taskManager.addIssue(new Epic(taskManager.incEpicIdCounter(), "epic2", "epic2_desc"));
        int s1Id = taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(), "subtask1", "subtask1_desc", taskManager.getIssueById(e1Id, IssueTypes.EPIC).getId()));
        int s2Id = taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(), "subtask2", "subtask2_desc", taskManager.getIssueById(e1Id, IssueTypes.EPIC).getId()));
        int s3Id = taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(), "subtask3", "subtask3_desc", taskManager.getIssueById(e2Id, IssueTypes.EPIC).getId()));
        int s4Id = taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(), "subtask4", "subtask4_desc", taskManager.getIssueById(e2Id, IssueTypes.EPIC).getId()));
        taskManager.printAllIssues();
        System.out.println("\nИзменение тасков и эпиков:");
        taskManager.updateIssue(t1Id, new Task(taskManager.incTaskIdCounter(), "task1_upd", "task1_desc_upd", Status.IN_PROGRESS));
        taskManager.updateIssue(t2Id, new Task(taskManager.incTaskIdCounter(), "task2_upd", "task2_desc_upd", Status.DONE));
        taskManager.updateIssue(e2Id, new Epic(taskManager.incEpicIdCounter(), "epic2_upd", "epic2_desc_upd", Status.DONE));
        System.out.println("\nИзменение сабтасков:");
        taskManager.updateIssue(s1Id, new Subtask(taskManager.incSubtaskIdCounter(), "subtask1_upd", "subtask1_desc_upd", Status.IN_PROGRESS));
        taskManager.updateIssue(s2Id, new Subtask(taskManager.incSubtaskIdCounter(), "subtask2_upd", "subtask2_desc_upd", Status.DONE));
        taskManager.updateIssue(s3Id, new Subtask(taskManager.incSubtaskIdCounter(),"subtask3_upd", "subtask3_desc_upd", Status.DONE));
        taskManager.updateIssue(s4Id, new Subtask(taskManager.incSubtaskIdCounter(),"subtask4_upd", "subtask4_desc_upd", Status.DONE));
        taskManager.printAllIssues();
        System.out.println("\nПросмотр каждой отдельной задачи:");
        System.out.println(taskManager.getIssueById(t1Id, IssueTypes.TASK).toString());
        System.out.println(taskManager.getIssueById(t2Id, IssueTypes.TASK).toString());
        System.out.println(taskManager.getIssueById(e1Id, IssueTypes.EPIC).toString());
        System.out.println(taskManager.getIssueById(e2Id, IssueTypes.EPIC).toString());
        System.out.println(taskManager.getIssueById(s1Id, IssueTypes.SUBTASK).toString());
        System.out.println(taskManager.getIssueById(s2Id, IssueTypes.SUBTASK).toString());
        System.out.println(taskManager.getIssueById(s3Id, IssueTypes.SUBTASK).toString());
        System.out.println(taskManager.getIssueById(s4Id, IssueTypes.SUBTASK).toString());
        System.out.println("\nУдаление:");
        taskManager.removeIssueById(t2Id, IssueTypes.TASK);
        taskManager.removeIssueById(s1Id, IssueTypes.SUBTASK);
        taskManager.removeIssueById(e2Id, IssueTypes.EPIC);
        taskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Task");
        taskManager.clearIssuesList(IssueTypes.TASK);
        taskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Subtask");
        taskManager.clearIssuesList(IssueTypes.SUBTASK);
        taskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Epic без сабтасков");
        taskManager.clearIssuesList(IssueTypes.EPIC);
        taskManager.printAllIssues();
        System.out.println("\nОчистка списка задач: Epic c сабтасками");
        int e1Id_new =taskManager.addIssue(new Epic(taskManager.incEpicIdCounter(), "epic1", "epic1_desc"));
        int e2Id_new = taskManager.addIssue(new Epic(taskManager.incEpicIdCounter(), "epic2", "epic2_desc"));
        taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(), "subtask1", "subtask1_desc", taskManager.getIssueById(e1Id_new, IssueTypes.EPIC).getId()));
        taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(),"subtask2", "subtask2_desc", taskManager.getIssueById(e1Id_new, IssueTypes.EPIC).getId()));
        taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(),"subtask3", "subtask3_desc", taskManager.getIssueById(e2Id_new, IssueTypes.EPIC).getId()));
        taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(),"subtask4", "subtask4_desc", taskManager.getIssueById(e2Id_new, IssueTypes.EPIC).getId()));
        System.out.println("\nДо очистки:");
        taskManager.printAllIssues();
        taskManager.clearIssuesList(IssueTypes.EPIC);
        System.out.println("\nПосле очистки:");
        taskManager.printAllIssues();
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
                    case 2 -> printIssue(taskManager);
                    case 3 -> updateIssue(taskManager);
                    case 4 -> removeIssue(taskManager);
                    case 5 -> taskManager.printIssues(chooseIssueType());
                    case 6 -> taskManager.printAllIssues();
                    case 7 -> taskManager.clearIssuesList(chooseIssueType());
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
                id = taskManager.addIssue(new Task(taskManager.incTaskIdCounter(), summary, description));
            }
            case EPIC -> {
                id = taskManager.addIssue(new Epic(taskManager.incEpicIdCounter(), summary, description));
            }
            case SUBTASK -> {
                if (taskManager.getIssuesList(IssueTypes.EPIC, Epic.class).size() == 0) {
                    System.out.println("Нет эпиков. Добавьте эпик");
                    return;
                }
                int epicId;
                System.out.println("Введите ID родительского эпика:");
                epicId = scanner.nextInt();
                scanner.nextLine();
                id = taskManager.addIssue(new Subtask(taskManager.incSubtaskIdCounter(), summary, description, taskManager.getIssueById(epicId, IssueTypes.EPIC).getId()));
            }
        }
        System.out.println("Задача добавлена. ID: " + id);
    }

    private static void printIssue(TaskManager taskManager) {
        IssueTypes type = chooseIssueType();
        System.out.println("Введите ID задачи, которую хотите посмотреть:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println(taskManager.getIssueById(id, type).toString());
    }

    private static void updateIssue(TaskManager taskManager) {
        IssueTypes type = chooseIssueType();
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
                case TASK -> taskManager.updateIssue(id, new Task(taskManager.incTaskIdCounter(), summary, description, newStatus));
                case EPIC -> taskManager.updateIssue(id, new Epic(taskManager.incEpicIdCounter(), summary, description, newStatus));
                case SUBTASK -> taskManager.updateIssue(id, new Subtask(taskManager.incSubtaskIdCounter(), summary, description, newStatus));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeIssue(TaskManager taskManager) {
        IssueTypes type = chooseIssueType();
        System.out.println("Введите ID задачи, которую хотите удалить:");
        int id = scanner.nextInt();
        scanner.nextLine();
        try {
            taskManager.removeIssueById(id, type);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
