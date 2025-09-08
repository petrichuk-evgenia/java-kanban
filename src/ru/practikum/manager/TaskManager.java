package ru.practikum.manager;

import ru.practikum.task.Task;

import java.util.List;

public interface TaskManager {
    public <T extends Task> int addIssue(T issue);

    public <T extends Task> T updateIssue(int id, T issue);

    public <T extends Task> List<T> getIssuesList(String issueType, Class<T> type);

    public void clearIssuesList(String issueType);

    public <T extends Task> T getIssueById(int id, String issueType);

    public void printIssues(String issueType);

    public void printAllIssues();

    public void removeIssueById(int id, String issueType);

    public HistoryManager getHistoryManager();
}
