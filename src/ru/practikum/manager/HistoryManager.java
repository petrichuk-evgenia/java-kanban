package ru.practikum.manager;

import ru.practikum.task.Task;

import java.util.List;

public interface HistoryManager {

    public <T extends Task> void addToHistory(T issue);

    public List<Object> getHistory();
}
