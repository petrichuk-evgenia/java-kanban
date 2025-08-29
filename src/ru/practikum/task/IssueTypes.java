package ru.practikum.task;

public enum IssueTypes {
    TASK("Task"),
    EPIC("Epic"),
    SUBTASK("Subtask");

    private String issueType;

    IssueTypes(String issueType) {
        this.issueType = issueType;
    }
}
