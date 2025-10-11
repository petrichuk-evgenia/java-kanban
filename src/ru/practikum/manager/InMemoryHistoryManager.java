/**
 * Реализация интерфейса {@link HistoryManager}, предоставляющая функционал для хранения истории задач в памяти.
 * Использует структуру {@link LinkedHashSet} для сохранения порядка добавления и исключения дубликатов.
 *
 * @author [Ваше имя]
 * @version 1.0
 * @since 1.0
 */
package ru.practikum.manager;

import ru.practikum.task.Task;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    /**
     * Набор задач, представляющий историю просмотров.
     * Элементы хранятся в порядке добавления и не повторяются.
     */
    private final Map<String, Object> history = new LinkedHashMap<>();

    /**
     * Добавляет указанную задачу в историю.
     * Если задача уже существует в истории, она будет удалена и заново добавлена,
     * чтобы поддерживать актуальный порядок.
     *
     * @param issue задача, которую нужно добавить
     * @param <T>   тип задачи, должен быть наследником класса {@link Task}
     */
    @Override
    public <T extends Task> void addToHistory(T issue) {
        if (issue != null) {
            String key = String.format("%d_%s", issue.getId(), issue.getClass().getSimpleName().toLowerCase(Locale.ROOT));
            if (history.containsKey(key)) {
                history.remove(key);
            }
            history.put(key, issue);
        }
    }

    /**
     * Возвращает набор задач, представляющих историю просмотров.
     *
     * @return история задач в виде множества
     */
    @Override
    public Map<String, Object> getHistory() {
        return history;
    }

    /**
     * Удаляет указанную задачу из истории.
     *
     * @param issue задача, которую нужно удалить
     * @param <T>   тип задачи, должен быть наследником класса {@link Task}
     */
    @Override
    public <T extends Task> void remove(T issue) {
        String key = String.format("%d_%s", issue.getId(), issue.getClass().getSimpleName().toLowerCase(Locale.ROOT));
        history.remove(key);
    }
}