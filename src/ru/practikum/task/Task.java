package ru.practikum.task;

public class Task {

    private int id;
    protected String summary;
    protected String description;
    protected Status status;

    public Task(int id, String summary, String description) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, String summary, String description, Status status) {
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

    public void setId(int id) {
        this.id = id;
    }
}
