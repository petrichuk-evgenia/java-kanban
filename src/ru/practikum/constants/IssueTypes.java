package ru.practikum.constants;

public enum IssueTypes {
    TASK("Task"),
    EPIC("Epic"),
    SUBTASK("Subtask");

    private String issueType;

    IssueTypes(String issueType) {
        this.issueType = issueType;
    }


    public String getIssueType() {
        return issueType;
    }
}
