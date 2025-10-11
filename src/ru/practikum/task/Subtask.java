package ru.practikum.task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String summary, String description, int epicId) {
        super(summary, description);
        this.epicId = epicId;
    }

    public Subtask(String summary, String description, Status status) {
        super(summary, description, status);
    }


    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "parentEpicId='" + epicId + '\'' +
                ", id='" + super.getId() + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }
}
