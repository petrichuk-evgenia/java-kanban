package ru.practikum;

import ru.practikum.constants.Status;

public class Task {

    protected int id = 0;
    protected String summary;
    protected String description;
    protected Status status;

    public Task(String summary, String description, int id) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String summary, String description, Status status, int id) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
