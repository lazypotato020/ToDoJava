import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    // Add a task and sort the list by priority
    public void addTask(Task task) {
        tasks.add(task);
        sortTasksByPriority();
    }

    // Edit a task
    public void editTask(Task oldTask, String newTitle, String newDescription, int newPriority) {
        oldTask.setTitle(newTitle);
        oldTask.setDescription(newDescription);
        oldTask.setPriority(newPriority);
        sortTasksByPriority();
    }

    // Delete a task
    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    // Get all tasks
    public List<Task> getTasks() {
        return tasks;
    }

    // Sort tasks by priority (highest priority first)
    private void sortTasksByPriority() {
        tasks.sort((task1, task2) -> Integer.compare(task2.getPriority(), task1.getPriority()));
    }
}
