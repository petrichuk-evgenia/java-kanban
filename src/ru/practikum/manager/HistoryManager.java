package ru.practikum.manager;

import ru.practikum.task.Task;

import java.util.List;

public interface HistoryManager {

    <T extends Task> void addToHistory(T issue);

    List<Object> getHistory();
}
