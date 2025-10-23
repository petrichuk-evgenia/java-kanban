package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practikum.manager.HistoryManager;
import ru.practikum.manager.InMemoryHistoryManager;
import ru.practikum.task.Epic;
import ru.practikum.task.Subtask;
import ru.practikum.task.Task;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Epic epic1;
    private Subtask subtask1;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description of Task 1");
        task1.setId(1);
        epic1 = new Epic("Epic 1", "Description of Epic 1");
        epic1.setId(1);
        subtask1 = new Subtask("Subtask 1", "Description of Subtask 1", 1);
        subtask1.setId(1);
    }

    @Test
    public <T extends Task> void testAddToHistoryAndGetHistory() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(epic1);
        historyManager.addToHistory(subtask1);

        Map<String, T> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertNotNull(history.get("1_task"));
        assertNotNull(history.get("1_epic"));
        assertNotNull(history.get("1_subtask"));
    }

    @Test
    public <T extends Task> void testAddDuplicateTaskShouldUpdateOrder() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task1); // Добавление дубликата

        Map<String, T> history = historyManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    public <T extends Task> void testRemoveFromHistory() {
        historyManager.addToHistory(task1);
        historyManager.addToHistory(epic1);
        historyManager.remove(task1);

        Map<String, T> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertNull(history.get("1_task"));
        assertNotNull(history.get("1_epic"));
    }

    @Test
    public <T extends Task> void testRemoveNonExistentTask() {
        historyManager.addToHistory(task1);
        historyManager.remove(epic1); // Попытка удалить несуществующую задачу

        Map<String, T> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertNotNull(history.get("1_task"));
    }

    @Test
    public <T extends Task> void testNullTaskIsIgnored() {
        historyManager.addToHistory(null);

        Map<String, T> history = historyManager.getHistory();
        assertEquals(0, history.size());
    }
}