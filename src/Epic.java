import java.util.ArrayList;

public class Epic extends Task {


    private final ArrayList<Integer> subTasks = new ArrayList<>();

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }


    public void addTask(Integer subTaskId) {
        subTasks.add(subTaskId);
    }


    public ArrayList<Integer> getSubTasksIds() {
        return subTasks;
    }

    public void removeSubTask(int subTaskId) {
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i) == subTaskId) {
                subTasks.remove(i);
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "Epic{" +

                "id=" + super.getId() +
                ", title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() + '\'' +
                ", subTasks IDs=" + subTasks +
                '}';
    }
}
