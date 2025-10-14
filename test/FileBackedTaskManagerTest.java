package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practikum.exceptions.ManagerSaveException;
import ru.practikum.manager.FileBackedTaskManager;
import ru.practikum.task.Epic;
import ru.practikum.task.Status;
import ru.practikum.task.Subtask;
import ru.practikum.task.Task;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование FileBackedTaskManager")
public class FileBackedTaskManagerTest{

    private static final String testFileName = "test/resources/test_tasks.txt";
    private FileBackedTaskManager taskManager = new FileBackedTaskManager("tasksList.txt");

    @AfterEach
    public void clearIssues() {
        taskManager.clearIssuesList("Task");
        taskManager.clearIssuesList("Epic");
        taskManager.clearIssuesList("Subtask");
        checkFiles();
    }

    @Test
    public void addTaskTest() {
        Task task = new Task("task1", "task1_desc");
        int t1Id = taskManager.addIssue(new Task("task1", "task1_desc"));
        task.setId(t1Id);
        assertTrue(taskManager.getIssueById(t1Id, "Task").equals(task));
        checkFiles();
    }

    @Test
    public void addEpicAndSubtaskTest() {
        Epic epic = new Epic("epic1", "epic1_desc");
        int e1Id = taskManager.addIssue(new Epic("epic1", "epic1_desc"));
        epic.setId(e1Id);
        Subtask subtask1 = new Subtask("subtask1", "subtask1_desc", epic.getId());
        int s1Id = taskManager.addIssue(new Subtask("subtask1", "subtask1_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        subtask1.setId(s1Id);
        Subtask subtask2 = new Subtask("subtask2", "subtask2_desc", epic.getId());
        int s2Id = taskManager.addIssue(new Subtask("subtask2", "subtask2_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        subtask2.setId(s2Id);
        epic.getSubtasks().add(s1Id);
        epic.getSubtasks().add(s2Id);
        assertTrue(Epic.class.cast(taskManager.getIssueById(e1Id, "Epic")).equals(epic));
        assertTrue(Subtask.class.cast(taskManager.getIssueById(s1Id, "Subtask")).equals(subtask1));
        assertTrue(Subtask.class.cast(taskManager.getIssueById(s2Id, "Subtask")).equals(subtask2));
        checkFiles();
    }

    @Test
    public void updateTaskTest() {
        Task task = new Task("task1", "task1_desc");
        int t1Id = taskManager.addIssue(new Task("task1", "task1_desc"));
        task.setId(t1Id);
        assertTrue(taskManager.getIssueById(t1Id, "Task").equals(task));
        Task taskToUpdate = new Task("task1_upd", "task1_desc_upd", Status.IN_PROGRESS);
        taskManager.updateIssue(t1Id, taskToUpdate);
        taskToUpdate.setId(t1Id);
        assertTrue(taskManager.getIssueById(t1Id, "Task").equals(taskToUpdate));
        checkFiles();
    }

    @Test
    public void updateEpicAndSubtaskTest() {
        int e1Id = taskManager.addIssue(new Epic("epic1", "epic1_desc"));
        int e2Id = taskManager.addIssue(new Epic("epic2", "epic2_desc"));
        int s1Id = taskManager.addIssue(new Subtask("subtask1", "subtask1_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s2Id = taskManager.addIssue(new Subtask("subtask2", "subtask2_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s3Id = taskManager.addIssue(new Subtask("subtask3", "subtask3_desc", taskManager.getIssueById(e2Id, "Epic").getId()));
        int s4Id = taskManager.addIssue(new Subtask("subtask4", "subtask4_desc", taskManager.getIssueById(e2Id, "Epic").getId()));
        taskManager.updateIssue(e2Id, new Epic("epic2_upd", "epic2_desc_upd"));
        taskManager.updateIssue(s1Id, new Subtask("subtask1_upd", "subtask1_desc_upd", Status.IN_PROGRESS));
        taskManager.updateIssue(s2Id, new Subtask("subtask2_upd", "subtask2_desc_upd", Status.DONE));
        taskManager.updateIssue(s3Id, new Subtask("subtask3_upd", "subtask3_desc_upd", Status.DONE));
        taskManager.updateIssue(s4Id, new Subtask("subtask4_upd", "subtask4_desc_upd", Status.DONE));
        Subtask updatedSubtask = taskManager.getIssueById(s1Id, "Subtask");
        assertTrue(updatedSubtask.getId() == s1Id &&
                updatedSubtask.getSummary().equals("subtask1_upd") &&
                updatedSubtask.getDescription().equals("subtask1_desc_upd") &&
                updatedSubtask.getStatus().equals(Status.IN_PROGRESS) &&
                updatedSubtask.getEpicId() == e1Id
        );
        taskManager.printAllIssues();
        Epic updatedEpic = taskManager.getIssueById(e2Id, "Epic");
        assertTrue(updatedEpic.getId() == e2Id &&
                updatedEpic.getSummary().equals("epic2_upd") &&
                updatedEpic.getDescription().equals("epic2_desc_upd") &&
                updatedEpic.getStatus().equals(Status.DONE)
        );
        assertEquals(taskManager.getIssueById(e1Id, "Epic").getStatus(), Status.IN_PROGRESS);
        checkFiles();

    }

    @Test
    public void removeIssueAndClearTest() {
        int t1Id = taskManager.addIssue(new Task("task1", "task1_desc"));
        int t2Id = taskManager.addIssue(new Task("task2", "task2_desc"));
        int e1Id = taskManager.addIssue(new Epic("epic1", "epic1_desc"));
        int e2Id = taskManager.addIssue(new Epic("epic2", "epic2_desc"));
        int s1Id = taskManager.addIssue(new Subtask("subtask1", "subtask1_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s2Id = taskManager.addIssue(new Subtask("subtask2", "subtask2_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s3Id = taskManager.addIssue(new Subtask("subtask3", "subtask3_desc", taskManager.getIssueById(e2Id, "Epic").getId()));
        int s4Id = taskManager.addIssue(new Subtask("subtask4", "subtask4_desc", taskManager.getIssueById(e2Id, "Epic").getId()));
        List<Integer> removedSubtasks = Epic.class.cast(taskManager.getIssueById(e2Id, "Epic")).getSubtasks();
        int parentEpicId = Subtask.class.cast(taskManager.getIssueById(s1Id, "Subtask")).getEpicId();
        System.out.println("\nУдаление:" + String.format(" Task %d, Subtask %d, Epic %d", t2Id, s1Id, e2Id));
        taskManager.removeIssueById(t2Id, "Task");
        checkRemoverIssueExists(taskManager.getIssuesList("Task", Task.class), t2Id);
        taskManager.removeIssueById(s1Id, "Subtask");
        checkRemoverIssueExists(taskManager.getIssuesList("Subtask", Subtask.class), s1Id);
        assertTrue(!Epic.class.cast(taskManager.getIssueById(parentEpicId, "Epic")).getSubtasks().contains(s1Id));
        taskManager.removeIssueById(e2Id, "Epic");
        taskManager.getIssuesList("Subtask", Subtask.class).forEach(subtask -> {
            removedSubtasks.forEach(removedSubtask -> {
                if (subtask.getId() == removedSubtask) {
                    assertTrue(!removedSubtasks.contains(removedSubtask));
                }
            });
        });
        checkFiles();
    }

    private <T extends Task> void checkRemoverIssueExists(List<T> issues, int removeId) {
        for (T issue : issues) {
            assertTrue(issue.getId() != removeId);
        }
    }

    @Test
    public void checkFilesForLoadFromFile() {
        int t1Id = taskManager.addIssue(new Task("task1", "task1_desc"));
        int t2Id = taskManager.addIssue(new Task("task2", "task2_desc"));
        int e1Id = taskManager.addIssue(new Epic("epic1", "epic1_desc"));
        int e2Id = taskManager.addIssue(new Epic("epic2", "epic2_desc"));
        int s1Id = taskManager.addIssue(new Subtask("subtask1", "subtask1_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s2Id = taskManager.addIssue(new Subtask("subtask2", "subtask2_desc", taskManager.getIssueById(e1Id, "Epic").getId()));
        int s3Id = taskManager.addIssue(new Subtask("subtask3", "subtask3_desc", taskManager.getIssueById(e2Id, "Epic").getId()));
        int s4Id = taskManager.addIssue(new Subtask("subtask4", "subtask4_desc", taskManager.getIssueById(e2Id, "Epic").getId()));
        try(BufferedReader reader1 = new BufferedReader(new FileReader(taskManager.getTasksFileName())); BufferedReader reader2 = new BufferedReader(new FileReader(testFileName));){
            while (reader1.ready() && reader2.ready()) {
                assertTrue(reader1.readLine().equals(reader2.readLine()), "Файлы не совпадают");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkFiles(){
        String fileName = "test/resources/tmp.txt";
        try {
            taskManager.save(fileName);
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        try(BufferedReader reader1 = new BufferedReader(new FileReader(taskManager.getTasksFileName())); BufferedReader reader2 = new BufferedReader(new FileReader(fileName));){
            while (reader1.ready() && reader2.ready()) {
                assertTrue(reader1.readLine().equals(reader2.readLine()), "Файлы не совпадают");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}