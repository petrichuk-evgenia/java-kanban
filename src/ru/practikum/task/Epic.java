package ru.practikum.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subtasks;

    public Epic(String summary, String description) {
        super(summary, description);
        this.subtasks = new ArrayList<>();
    }

    public Epic(String taskString) {
        super(taskString);
        String[] parts = taskString.substring(taskString.indexOf("{") + 1, taskString.indexOf("}")).split(", ");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subtasks.equals(epic.subtasks);
    }
}
