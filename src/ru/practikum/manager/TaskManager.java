package ru.practikum.manager;

import ru.practikum.task.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {
    <T extends Task> int addIssue(T issue);

    <T extends Task> T updateIssue(int id, T issue);

    <T extends Task> List<T> getIssuesList(String issueType, Class<T> type);

    void clearIssuesList(String issueType);

    <T extends Task> T getIssueById(int id, String issueType);

    void printIssues(String issueType);

    void printAllIssues();

    void removeIssueById(int id, String issueType);

    <T extends Task> Map<String, T> getHistory();
}
