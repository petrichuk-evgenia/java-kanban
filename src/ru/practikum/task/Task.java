package ru.practikum.task;

public class Task {

    protected String summary;
    protected String description;
    protected Status status;
    private int id;

    public Task(String summary, String description) {
        this.id = 0;
        this.summary = summary;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String summary, String description, Status status) {
        this.id = 0;
        this.summary = summary;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && summary.equals(task.summary) && description.equals(task.description) && status == task.status;
    }
}
