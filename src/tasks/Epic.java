package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {


    private final ArrayList<Integer> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        // при создании эпика без подзадач его длительность 0 минут и время начала текущее
        super(title, description, TaskStatus.NEW, LocalDateTime.now(), 0);

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
                ", startTime=" + super.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", duration=" + super.getDuration() +
                ", subTasks IDs=" + subTasks +
                '}';
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
