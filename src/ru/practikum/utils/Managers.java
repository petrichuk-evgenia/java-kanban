package ru.practikum.utils;

import ru.practikum.manager.*;

import static ru.practikum.manager.FileBackedTaskManager.TASKS_FILE_NAME;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTaskManager(TASKS_FILE_NAME);
    }
}
