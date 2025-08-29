package ru.practikum.task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String summary, String description, int epicId) {
        super(id, summary, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String summary, String description, Status status) {
        super(id, summary, description, status);
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
}
