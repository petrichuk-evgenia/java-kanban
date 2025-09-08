package ru.practikum.manager;

import ru.practikum.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Object> history = new ArrayList<>();

    /**
     * Добавляет указанную задачу в историю просмотров.
     * <p>
     * Если размер истории достигает максимального значения (10 элементов),
     * самый старый элемент удаляется из начала списка перед добавлением нового.
     *
     * @param issue задача, которую необходимо добавить в историю
     */
    @Override
    public <T extends Task> void addToHistory(T issue) {
        if (issue != null){
            if (history.size() == 10) {
                history.remove(0);
            }
            history.add(issue);
        }
    }

    @Override
    public List<Object> getHistory() {
        return history;
    }
}
