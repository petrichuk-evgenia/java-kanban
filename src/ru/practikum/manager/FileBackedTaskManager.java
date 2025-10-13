/**
 * Класс FileBackedTaskManager реализует интерфейс TaskManager и предоставляет функциональность
 * для сохранения и загрузки задач из файла. Все операции с задачами (добавление, обновление, удаление)
 * автоматически сохраняются в файл, а при запуске программы данные загружаются из него.
 */
package ru.practikum.manager;

import ru.practikum.exceptions.ManagerSaveException;
import ru.practikum.task.Epic;
import ru.practikum.task.Subtask;
import ru.practikum.task.Task;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager implements TaskManager {

    /**
     * Имя файла, в котором хранятся задачи.
     */
    public static final String TASKS_FILE_NAME = "tasksList.txt";

    /**
     * Экземпляр менеджера задач, используемый для хранения данных в памяти.
     */
    private static TaskManager manager;

    /**
     * Конструктор класса FileBackedTaskManager.
     * Создаёт новый менеджер задач, загружая данные из указанного файла.
     * Если файл не существует или произошла ошибка при чтении,
     * исключение игнорируется, и создаётся новый пустой InMemoryTaskManager.
     *
     * @param fileName Путь к файлу, из которого необходимо загрузить данные
     */
    public FileBackedTaskManager(String fileName) {
        manager = new InMemoryTaskManager();
        try {
            loadFromFile(new File(fileName));
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Загружает задачи из указанного файла в менеджер.
     *
     * @param file файл, из которого будут загружены задачи
     * @throws ManagerSaveException если файл не существует, не является файлом или произошла ошибка при чтении
     */
    public static void loadFromFile(File file) throws ManagerSaveException {
        if (!file.exists() || !file.isFile()) {
            throw new ManagerSaveException("Файл задач не является файлом или не существует");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String issue = reader.readLine();
                String issueType = issue.split("\\{")[0];
                switch (issueType) {
                    case "Task" -> manager.addIssue(new Task(issue));
                    case "Subtask" -> manager.addIssue(new Subtask(issue));
                    case "Epic" -> manager.addIssue(new Epic(issue));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Сохраняет текущие задачи в файл.
     *
     * @throws ManagerSaveException если произошла ошибка при записи в файл
     */
    public void save(String fileName) throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(fileName)) {
            getTasksList().get("Epic").forEach(issue -> convertTaskToString(writer, "Epic", issue));
            getTasksList().get("Subtask").forEach(issue -> convertTaskToString(writer, "Subtask", issue));
            getTasksList().get("Task").forEach(issue -> convertTaskToString(writer, "Task", issue));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    /**
     * Преобразует задачу в строку и записывает её в файл.
     *
     * @param writer объект FileWriter для записи в файл
     * @param type   тип задачи ("Task", "Subtask", "Epic")
     * @param issue  объект задачи
     */
    private void convertTaskToString(FileWriter writer, String type, Object issue) {
        try {
            switch (type) {
                case "Task" -> writer.write(((Task) issue).toString() + "\n");
                case "Subtask" -> writer.write(((Subtask) issue).toString() + "\n");
                case "Epic" -> writer.write(((Epic) issue).toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Добавляет новую задачу в менеджер и сохраняет изменения в файл.
     *
     * @param issue задача, которую нужно добавить
     * @return идентификатор новой задачи
     */
    @Override
    public <T extends Task> int addIssue(T issue) {
        int id = manager.addIssue(issue);
        try {
            save(TASKS_FILE_NAME);
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    /**
     * Обновляет задачу в менеджере и сохраняет изменения в файл.
     *
     * @param id    идентификатор задачи
     * @param issue обновленная задача
     * @return обновленная задача
     */
    @Override
    public <T extends Task> T updateIssue(int id, T issue) {
        T updatedIssue = manager.updateIssue(id, issue);
        try {
            save(TASKS_FILE_NAME);
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return updatedIssue;
    }

    /**
     * Возвращает список задач указанного типа.
     *
     * @param issueType тип задачи ("Task", "Subtask", "Epic")
     * @param type      класс задачи
     * @return список задач указанного типа
     */
    @Override
    public <T extends Task> List<T> getIssuesList(String issueType, Class<T> type) {
        return manager.getIssuesList(issueType, type);
    }

    /**
     * Удаляет все задачи указанного типа из менеджера и сохраняет изменения в файл.
     *
     * @param issueType тип задачи ("Task", "Subtask", "Epic")
     */
    @Override
    public void clearIssuesList(String issueType) {
        manager.clearIssuesList(issueType);
        try {
            save(TASKS_FILE_NAME);
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Получает задачу по её идентификатору и типу.
     *
     * @param id        идентификатор задачи
     * @param issueType тип задачи ("Task", "Subtask", "Epic")
     * @return задача с указанным идентификатором
     */
    @Override
    public <T extends Task> T getIssueById(int id, String issueType) {
        return manager.getIssueById(id, issueType);
    }

    /**
     * Выводит список задач указанного типа на экран.
     *
     * @param issueType тип задачи ("Task", "Subtask", "Epic")
     */
    @Override
    public void printIssues(String issueType) {
        manager.printIssues(issueType);
    }

    /**
     * Выводит содержимое файла задач на экран.
     */
    @Override
    public void printAllIssues() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE_NAME))) {
            while (reader.ready()) {
                System.out.println(reader.readLine());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Удаляет задачу с указанным идентификатором и типом из менеджера и сохраняет изменения в файл.
     *
     * @param id        идентификатор задачи
     * @param issueType тип задачи ("Task", "Subtask", "Epic")
     */
    @Override
    public void removeIssueById(int id, String issueType) {
        manager.removeIssueById(id, issueType);
        try {
            save(TASKS_FILE_NAME);
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Возвращает историю просмотров задач.
     *
     * @return карта, содержащая историю просмотров задач
     */
    @Override
    public <T extends Task> Map<String, T> getHistory() {
        return manager.getHistory();
    }

    /**
     * Возвращает список всех задач, разбитый по типам.
     *
     * @return карта, где ключ - тип задачи, значение - список задач
     */
    @Override
    public Map<String, List<Object>> getTasksList() {
        return manager.getTasksList();
    }
}