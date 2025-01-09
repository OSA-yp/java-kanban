package tasks;

import java.util.ArrayList;

public class Epic extends Task {


    private final ArrayList<Integer> subTasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }


    public void addSubTask(Integer subTaskId) {
        subTasks.add(subTaskId);
    }


    public ArrayList<Integer> getSubTasksIds() {
        return subTasks;
    }

    public void removeSubTask(Integer subTaskId) {
        subTasks.remove(subTaskId);
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }



    @Override
    public String toString() {
        return "Task.Epic{" +

                "id=" + super.getId() +
                ", title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() + '\'' +
                ", subTasks IDs=" + subTasks +
                '}';
    }
}
