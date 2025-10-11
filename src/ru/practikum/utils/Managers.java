package ru.practikum.utils;

import ru.practikum.manager.HistoryManager;
import ru.practikum.manager.InMemoryHistoryManager;
import ru.practikum.manager.InMemoryTaskManager;
import ru.practikum.manager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
