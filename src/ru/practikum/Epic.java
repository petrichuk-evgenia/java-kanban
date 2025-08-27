package ru.practikum;

import ru.practikum.constants.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public List<Integer> subtasks;

    public Epic(String summary, String description, int id) {
        super(summary, description, id);
        this.subtasks = new ArrayList<>();
    }

    public Epic(String summary, String description, Status status, int id) {
        super(summary, description, status, id);
        this.subtasks = new ArrayList<>();
    }


    public List<Integer> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtasks.toString() +
                ", id='" + id + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
