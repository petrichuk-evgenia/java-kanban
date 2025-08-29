package ru.practikum.manager;

import ru.practikum.task.IssueTypes;
import ru.practikum.task.Status;
import ru.practikum.task.Epic;
import ru.practikum.task.Subtask;
import ru.practikum.task.Task;

import java.util.*;

import static ru.practikum.task.IssueTypes.*;

public class TaskManager {

    //список задач
    private Map<IssueTypes, List<Object>> tasksList;
    private int taskIdCounter;
    private int subtaskIdCounter;
    private int epicIdCounter;


    public TaskManager() {
        taskIdCounter = 0;
        subtaskIdCounter = 0;
        epicIdCounter = 0;
        tasksList = new LinkedHashMap<>();
        tasksList.put(TASK, new ArrayList<>());
        tasksList.put(SUBTASK, new ArrayList<>());
        tasksList.put(EPIC, new ArrayList<>());
    }

    /**
     * Добавляет задачу в соответствующий список в зависимости от её типа.
     *
     * @param <T>   Тип задачи, расширяющий класс Task
     * @param issue Задача, которую необходимо добавить
     * @return Идентификатор добавленной задачи
     */
    public <T extends Task> int addIssue(T issue) {
        Class<?> issueClass = issue.getClass();
        switch (issueClass.getSimpleName()) {
            case "Task" -> tasksList.get(TASK).add(issue);
            case "Subtask" -> {
                tasksList.get(SUBTASK).add(issue);
                getIssueByClass(Subtask.class.cast(issue).getEpicId(), Epic.class).getSubtasks().add(issue.getId());
            }
            case "Epic" -> tasksList.get(EPIC).add(issue);
        }
        return issue.getId();
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
    public <T extends Task> T updateIssue(int id, T issue) {
        T updatedIssue = getIssueById(id, IssueTypes.valueOf(issue.getClass().getSimpleName().toUpperCase(Locale.ROOT)));
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
    public <T extends Task> List<T> getIssuesList(IssueTypes issueType, Class<T> type) {
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
    public void clearIssuesList(IssueTypes issueType) {
        if (issueType == IssueTypes.SUBTASK) {
            for (int i = 0; i < tasksList.get(EPIC).size(); i++) {
                Epic epic = Epic.class.cast(tasksList.get(EPIC).get(i));
                epic.getSubtasks().clear();
                updateEpicStatus(epic.getId());
            }
        }
        if (issueType == IssueTypes.EPIC) {
            tasksList.get(SUBTASK).clear();
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
    public <T extends Task> T getIssueById(int id, IssueTypes issueType) {
        switch (issueType) {
            case TASK -> {
                return (T) getIssueByClass(id, Task.class);
            }
            case EPIC -> {
                return (T) getIssueByClass(id, Epic.class);
            }
            case SUBTASK -> {
                return (T) getIssueByClass(id, Subtask.class);
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

        for (T i : getIssuesList(IssueTypes.valueOf(type.getSimpleName().toUpperCase(Locale.ROOT)), type)) {
            if (i.getId() == id) {
                issue = i;
            }
        }
        if (issue == null) {
            throw new IllegalArgumentException("Задача с таким идентификатором не найдена");
        }
        return issue;
    }

    /**
     * Выводит информацию о задачах определенного типа.
     *
     * @param issueType Тип задач, которые необходимо вывести (TASK, SUBTASK, EPIC).
     */
    public void printIssues(IssueTypes issueType) {
        for (Object issue : tasksList.get(issueType)) {
            switch (issueType) {
                case TASK -> printIssue((Task) issue);
                case SUBTASK -> printIssue((Subtask) issue);
                case EPIC -> printIssue((Epic) issue);
            }

        }
    }

    /**
     * Выводит информацию обо всех задачах, перебирая все типы задач.
     * Для каждого типа задач вызывается метод printIssues().
     */
    public void printAllIssues() {
        for (IssueTypes issueType : IssueTypes.values()) {
            printIssues(issueType);
        }
    }

    /**
     * Вспомогательный метод для вывода информации о конкретной задаче.
     *
     * @param issue Объект задачи, который нужно вывести.
     * @param <T>   Тип задачи, должен быть подклассом {@link Task}.
     */
    private static <T extends Task> void printIssue(T issue) {
        System.out.println(issue.toString());
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
    public void removeIssueById(int id, IssueTypes issueType) {
        if (issueType == IssueTypes.SUBTASK) {
            int parentId = Subtask.class.cast(getIssueById(id, issueType)).getEpicId();
            getIssueByClass(parentId, Epic.class).getSubtasks().removeIf(i -> i == id);
            updateEpicStatus(parentId);
        }
        if (issueType == IssueTypes.EPIC) {
            for (int subtaskId : getIssueByClass(id, Epic.class).getSubtasks()) {
                removeIssue(subtaskId, IssueTypes.SUBTASK);
            }
        }
        removeIssue(id, issueType);
    }

    private void removeIssue(int id, IssueTypes issueType) {
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

    public int incTaskIdCounter() {
        this.taskIdCounter++;
        return this.taskIdCounter;
    }

    public int incSubtaskIdCounter() {
        this.subtaskIdCounter++;
        return this.subtaskIdCounter;
    }

    public int incEpicIdCounter() {
        this.epicIdCounter++;
        return this.epicIdCounter;
    }
}
