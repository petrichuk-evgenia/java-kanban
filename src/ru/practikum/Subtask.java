package ru.practikum;

import ru.practikum.constants.Status;

public class Subtask extends Task {

    private int parentEpicId;

    public Subtask(String summary, String description, int parentEpicId, int id) {
        super(summary, description, id);
        this.parentEpicId = parentEpicId;
    }

    public Subtask(String summary, String description, Status status, int id) {
        super(summary, description, status, id);
    }


    public int getParentEpicId() {
        return parentEpicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "parentEpicId='" + parentEpicId + '\'' +
                ", id='" + id + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
