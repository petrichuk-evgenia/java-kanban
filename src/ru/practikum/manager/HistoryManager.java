package ru.practikum.manager;

import ru.practikum.task.Task;

import java.util.Map;

public interface HistoryManager {

    <T extends Task> void addToHistory(T issue);

    <T extends Task> Map<String, T> getHistory();

    <T extends Task> void remove(T issue);
}
