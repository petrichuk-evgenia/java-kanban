package ru.practikum.manager;

import ru.practikum.task.Epic;
import ru.practikum.task.Status;
import ru.practikum.task.Subtask;
import ru.practikum.task.Task;
import ru.practikum.utils.Managers;

import java.util.*;

public class InMemoryTaskManager<T extends Task> implements TaskManager {

    //список задач
    private Map<String, List<Object>> tasksList;
    private int taskIdCounter;
    private int subtaskIdCounter;
    private int epicIdCounter;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        taskIdCounter = 0;
        subtaskIdCounter = 0;
        epicIdCounter = 0;
        historyManager = Managers.getDefaultHistory();
        tasksList = new LinkedHashMap<>();
        tasksList.put(Task.class.getSimpleName(), new LinkedList<>());
        tasksList.put(Subtask.class.getSimpleName(), new LinkedList<>());
        tasksList.put(Epic.class.getSimpleName(), new LinkedList<>());
    }

    /**
     * Вспомогательный метод для вывода информации о конкретной задаче.
     *
     * @param issue Объект задачи, который нужно вывести.
     * @param <T>   Тип задачи, должен быть подклассом {@link Task}.
     */
    public static <T extends Task> void printIssue(T issue) {
        System.out.println(issue.toString());
    }

    /**
     * Добавляет задачу в соответствующий список в зависимости от её типа.
     *
     * @param issue Задача, которую необходимо добавить
     * @return Идентификатор добавленной задачи
     */
    @Override
    public <T extends Task> int addIssue(T issue) {
        String issueType = issue.getClass().getSimpleName();
        switch (issueType) {
            case "Task" -> {
                updateId(issue, incTaskIdCounter());
                tasksList.get(issueType).add(issue);
            }
            case "Subtask" -> {
                updateId(issue, incSubtaskIdCounter());
                tasksList.get(issueType).add(issue);
                Epic parentEpic = getIssueByClass(Subtask.class.cast(issue).getEpicId(), Epic.class);
                parentEpic.getSubtasks().add(issue.getId());
                updateEpicStatus(parentEpic.getId());
            }
            case "Epic" -> {
                updateId(issue, incEpicIdCounter());
                tasksList.get(issueType).add(issue);
            }
        }
        historyManager.addToHistory(issue);
        return issue.getId();
    }

    private <T extends Task> void updateId(T issue, int id) {
        if (issue.getId() == 0) {
            issue.setId(id);
        }
    }

    /**
     * Обновляет существующую задачу на основе переданной задачи `issue`.
     * <p>
     * Метод ищет задачу по её идентификатору `id` и типу (определяемому через имя класса),
     * затем обновляет описание и заголовок. Для подзадач также обновляется статус и
     * вызывается метод обновления статуса эпика.
     *
     * @param id    идентификатор задачи, которую нужно обновить
     * @param issue новая версия задачи с обновлёнными данными
     * @return обновлённую задачу
     * @throws IllegalArgumentException если задача с указанным id не найдена
     */
    @Override
    public <T extends Task> T updateIssue(int id, T issue) {
        T updatedIssue = getIssueById(id, issue.getClass().getSimpleName());
        updatedIssue.setDescription(issue.getDescription());
        updatedIssue.setSummary(issue.getSummary());
        switch (updatedIssue.getClass().getSimpleName()) {
            case "Subtask" -> {
                updatedIssue.setStatus(issue.getStatus());
                updateEpicStatus(Subtask.class.cast(updatedIssue).getEpicId());
            }
            case "Epic" -> {
                return updatedIssue;
            }
            default -> {
                updatedIssue.setStatus(issue.getStatus());
                return updatedIssue;
            }
        }
        historyManager.addToHistory(updatedIssue);
        return updatedIssue;
    }

    /**
     * Обновляет статус эпика на основе статусов его подзадач.
     * <p>
     * Если у эпика нет подзадач, его статус устанавливается в {@code NEW}.
     * Иначе, метод подсчитывает количество подзадач со статусами {@code NEW} и {@code DONE},
     * и на основе этого определяет общий статус эпика:
     * - все подзадачи NEW → статус эпика NEW;
     * - все подзадачи DONE → статус эпика DONE;
     * - иначе → статус эпика IN_PROGRESS.
     *
     * @param id идентификатор эпика, для которого нужно обновить статус
     */
    private void updateEpicStatus(int id) {
        Epic issue = getIssueByClass(id, Epic.class);
        if (issue.getSubtasks().isEmpty()) {
            issue.setStatus(Status.NEW);
            return;
        }

        int subtasksNewStatus = 0;
        int subtasksDoneStatus = 0;
        int totalSubtasks = issue.getSubtasks().size();

        for (int subtaskId : issue.getSubtasks()) {
            Subtask subtask = getIssueByClass(subtaskId, Subtask.class);
            if (subtask.getStatus() == Status.NEW) {
                subtasksNewStatus++;
            } else if (subtask.getStatus() == Status.DONE) {
                subtasksDoneStatus++;
            }
        }

        if (subtasksNewStatus == totalSubtasks) {
            issue.setStatus(Status.NEW);
        } else if (subtasksDoneStatus == totalSubtasks) {
            issue.setStatus(Status.DONE);
        } else {
            issue.setStatus(Status.IN_PROGRESS);
        }
    }

    /**
     * Возвращает список задач указанного типа.
     *
     * @param issueType Тип задач, которые нужно получить (TASK, SUBTASK, EPIC).
     * @return Список задач заданного типа.
     */
    @Override
    public <T extends Task> List<T> getIssuesList(String issueType, Class<T> type) {
        List<T> list = new ArrayList<>();
        tasksList.get(issueType).forEach(issue -> {
            list.add(type.cast(issue));
        });
        return list;
    }

    /**
     * Очищает список задач указанного типа.
     *
     * @param issueType Тип задач, чей список нужно очистить (TASK, SUBTASK, EPIC).
     */
    @Override
    public void clearIssuesList(String issueType) {
        switch (issueType) {
            case "Task" -> getIssuesList(issueType, Task.class).forEach(issue -> historyManager.remove(issue));
            case "Subtask" -> getIssuesList(issueType, Subtask.class).forEach(issue -> historyManager.remove(issue));
            case "Epic" -> getIssuesList(issueType, Epic.class).forEach(issue -> {
                issue.getSubtasks().forEach(subtaskId -> {
                    if (getIssueById(subtaskId, "Subtask") != null) {
                        historyManager.remove(getIssueById(subtaskId, "Subtask"));
                    }
                });
                historyManager.remove(issue);
            });
        }
        if (issueType.equals("Subtask")) {
            for (int i = 0; i < tasksList.get("Epic").size(); i++) {
                Epic epic = Epic.class.cast(tasksList.get("Epic").get(i));
                epic.getSubtasks().clear();
                updateEpicStatus(epic.getId());
            }
        }
        if (issueType.equals("Epic")) {
            tasksList.get("Subtask").clear();
        }
        tasksList.get(issueType).clear();
    }

    /**
     * Возвращает задачу по её идентификатору. Если идентификатор соответствует типу TASK,
     * производится поиск задачи в списке.
     *
     * @param <T> Тип задачи, расширяющий класс Task
     * @param id  Идентификатор задачи
     * @return Найденная задача типа T или null, если задача не найдена
     */
    @Override
    public <T extends Task> T getIssueById(int id, String issueType) {
        switch (issueType) {
            case "Task" -> {
                T issue = (T) getIssueByClass(id, Task.class);
                return issue;
            }
            case "Epic" -> {
                T issue = (T) getIssueByClass(id, Epic.class);
                return issue;
            }
            case "Subtask" -> {
                T issue = (T) getIssueByClass(id, Subtask.class);
                return issue;
            }
        }
        return null;
    }

    /**
     * Возвращает элемент по его идентификатору из списка, полученного через {@code getIssuesList}.
     *
     * @param <T>  Тип элемента, расширяющий класс Task
     * @param id   Идентификатор элемента
     * @param type Класс типа T, используемый для параметризации
     * @return Найденный элемент типа T или null, если элемент не найден
     */
    private <T extends Task> T getIssueByClass(int id, Class<T> type) {
        T issue = null;

        for (T i : getIssuesList(type.getSimpleName(), type)) {
            if (i.getId() == id) {
                issue = i;
            }
        }
        if (issue == null) {
            throw new IllegalArgumentException("Задача с таким идентификатором не найдена");
        }
        historyManager.addToHistory(issue);
        return issue;
    }

    /**
     * Выводит информацию о задачах определенного типа.
     *
     * @param issueType Тип задач, которые необходимо вывести (TASK, SUBTASK, EPIC).
     */
    @Override
    public void printIssues(String issueType) {
        for (Object issue : tasksList.get(issueType)) {
            switch (issueType) {
                case "Task" -> printIssue((Task) issue);
                case "Subtask" -> printIssue((Subtask) issue);
                case "Epic" -> printIssue((Epic) issue);
            }

        }
    }

    /**
     * Выводит информацию обо всех задачах, перебирая все типы задач.
     * Для каждого типа задач вызывается метод printIssues().
     */
    @Override
    public void printAllIssues() {
        printIssues("Task");
        printIssues("Subtask");
        printIssues("Epic");
    }

    /**
     * Удаляет задачу по её идентификатору и типу.
     * <p>
     * Если удаляемая задача является подзадачей, метод также удаляет её из списка подзадач соответствующего эпика.
     * Затем задача удаляется из глобального списка задач, соответствующего указанному типу.
     *
     * @param id        идентификатор задачи, которую нужно удалить
     * @param issueType тип задачи (TASK, SUBTASK, EPIC)
     * @throws IllegalArgumentException если задача с указанным id не найдена
     */
    @Override
    public void removeIssueById(int id, String issueType) {
        if (issueType.equals("Subtask")) {
            int parentId = Subtask.class.cast(getIssueById(id, issueType)).getEpicId();
            getIssueByClass(parentId, Epic.class).getSubtasks().removeIf(i -> i == id);
            updateEpicStatus(parentId);
        }
        if (issueType.equals("Epic")) {
            for (int subtaskId : getIssueByClass(id, Epic.class).getSubtasks()) {
                removeIssue(subtaskId, "Subtask");
            }
        }
        removeIssue(id, issueType);
    }

    @Override
    public <T extends Task> Map<String, T> getHistory() {
        return historyManager.getHistory();
    }

    private void removeIssue(int id, String issueType) {
        historyManager.remove(getIssueById(id, issueType));
        tasksList.get(issueType).removeIf(issue -> {
            if (issue instanceof Task) {
                return ((Task) issue).getId() == id;
            } else if (issue instanceof Subtask) {
                return ((Subtask) issue).getId() == id;
            } else {
                return ((Epic) issue).getId() == id;
            }
        });
    }

    private int incTaskIdCounter() {
        this.taskIdCounter++;
        return this.taskIdCounter;
    }

    private int incSubtaskIdCounter() {
        this.subtaskIdCounter++;
        return this.subtaskIdCounter;
    }

    private int incEpicIdCounter() {
        this.epicIdCounter++;
        return this.epicIdCounter;
    }
}
