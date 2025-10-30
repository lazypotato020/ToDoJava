import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    private TaskManager taskManager = new TaskManager();
    private ObservableList<Task> observableTasks;
    private HBox mainLayout; // Declare mainLayout as an instance variable

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Task ListView
        ListView<Task> taskListView = new ListView<>();
        observableTasks = FXCollections.observableArrayList(taskManager.getTasks());
        taskListView.setItems(observableTasks);

        // Input Fields
        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Task Description");

        TextField priorityField = new TextField();
        priorityField.setPromptText("Priority (1-5)");

        // Priority Filter ComboBox with "All" option
        ComboBox<String> priorityFilter = new ComboBox<>();
        priorityFilter.setItems(FXCollections.observableArrayList("All", "1", "2", "3", "4", "5"));
        priorityFilter.setPromptText("Filter by Priority");

        priorityFilter.setOnAction(e -> {
            String selectedPriority = priorityFilter.getValue();
            if (selectedPriority != null) {
                if (selectedPriority.equals("All")) {
                    observableTasks.setAll(taskManager.getTasks()); // Show all tasks if "All" is selected
                } else {
                    int priority = Integer.parseInt(selectedPriority);
                    filterTasksByPriority(priority);
                }
            }
        });

        // Buttons
        Button addButton = new Button("Add Task");
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            try {
                int priority = Integer.parseInt(priorityField.getText());
                if (priority < 1 || priority > 5) throw new NumberFormatException();

                Task newTask = new Task(title, description, priority);
                taskManager.addTask(newTask);
                observableTasks.setAll(taskManager.getTasks());

                titleField.clear();
                descriptionField.clear();
                priorityField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Priority", "Priority must be a number between 1 and 5.");
            }
        });

        Button deleteButton = new Button("Delete Selected Task");
        deleteButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                taskManager.deleteTask(selectedTask);
                observableTasks.setAll(taskManager.getTasks());
            }
        });

        Button editButton = new Button("Edit Selected Task");
        editButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                titleField.setText(selectedTask.getTitle());
                descriptionField.setText(selectedTask.getDescription());
                priorityField.setText(String.valueOf(selectedTask.getPriority()));

                addButton.setDisable(true); // Disable Add button to avoid conflicts
                Button saveButton = new Button("Save Changes");
                saveButton.setOnAction(ev -> {
                    try {
                        String newTitle = titleField.getText();
                        String newDescription = descriptionField.getText();
                        int newPriority = Integer.parseInt(priorityField.getText());
                        if (newPriority < 1 || newPriority > 5) throw new NumberFormatException();

                        taskManager.editTask(selectedTask, newTitle, newDescription, newPriority);
                        observableTasks.setAll(taskManager.getTasks());

                        titleField.clear();
                        descriptionField.clear();
                        priorityField.clear();
                        mainLayout.getChildren().remove(saveButton); // Use the instance variable
                        addButton.setDisable(false);
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid Priority", "Priority must be a number between 1 and 5.");
                    }
                });

                mainLayout.getChildren().add(saveButton); // Use the instance variable
            }
        });

        // Layout
        VBox inputLayout = new VBox(10, titleField, descriptionField, priorityField, addButton, editButton, deleteButton, priorityFilter);
        inputLayout.setPadding(new Insets(10));
        taskListView.setPrefWidth(300);

        mainLayout = new HBox(10, inputLayout, taskListView); // Initialize the instance variable
        mainLayout.setPadding(new Insets(10));

        // Scene and Stage
        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TODO List Application");
        primaryStage.show();
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Filter tasks by priority
    private void filterTasksByPriority(int priority) {
        ObservableList<Task> filteredTasks = FXCollections.observableArrayList();
        for (Task task : taskManager.getTasks()) {
            if (task.getPriority() == priority) {
                filteredTasks.add(task);
            }
        }
        observableTasks.setAll(filteredTasks);
    }
}


// // java --module-path C:\Users\PMYLS\Downloads\openjfx-23.0.1_windows-x64_bin-sdk\javafx-sdk-23.0.1\lib --add-modules javafx.controls -cp bin Main

// // javac --module-path C:\Users\PMYLS\Downloads\openjfx-23.0.1_windows-x64_bin-sdk\javafx-sdk-23.0.1\lib --add-modules javafx.controls -d bin src/*.java
