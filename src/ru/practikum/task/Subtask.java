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

    public Subtask(String taskString) {
        super(taskString);
        String[] parts = taskString.substring(taskString.indexOf("{") + 1, taskString.indexOf("}")).split(", ");

        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().trim().replaceAll("'", "");
                String value = keyValue[1].trim().trim().replaceAll("'", "");
                if (key.equals("parentEpicId")) {
                    this.epicId = Integer.parseInt(value);
                }
            }
        }
    }

}
