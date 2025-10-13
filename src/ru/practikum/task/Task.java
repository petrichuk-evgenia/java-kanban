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

    public Task(String taskString) {
        String[] parts = taskString.substring(taskString.indexOf("{") + 1, taskString.indexOf("}")).split(", ");
        this.id = 0;
        this.summary = "";
        this.description = "";
        this.status = Status.NEW;

        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("'", "");
                String value = keyValue[1].trim().replaceAll("'", "");

                switch (key) {
                    case "id":
                        this.id = Integer.parseInt(value);
                        break;
                    case "summary":
                        this.summary = value;
                        break;
                    case "description":
                        this.description = value;
                        break;
                    case "status":
                        this.status = Status.valueOf(value);
                        break;
                }
            }
        }
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
