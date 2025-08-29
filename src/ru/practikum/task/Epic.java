package ru.practikum.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subtasks;

    public Epic(int id, String summary, String description) {
        super(id, summary, description);
        this.subtasks = new ArrayList<>();
    }

    public Epic(int id, String summary, String description, Status status) {
        super(id, summary, description, status);
        this.subtasks = new ArrayList<>();
    }


    public List<Integer> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtasks.toString() +
                ", id='" + super.getId() + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
