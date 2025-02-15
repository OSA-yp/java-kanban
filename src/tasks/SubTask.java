package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {


    private final Integer parentEpicId;

    public SubTask(String title, String description, TaskStatus status, LocalDateTime startTime, long durationInMins, Integer parentEpicId) {
        super(title, description, status, startTime, durationInMins);
        this.parentEpicId = parentEpicId;
    }

    public SubTask(String title, String description, TaskStatus status, Integer parentEpicId) {
        super(title, description, status, null, 0);
        this.parentEpicId = parentEpicId;
    }


    public Integer getParentEpicId() {
        return parentEpicId;
    }


    @Override
    public String toString() {
        return "Task.SubTask{" +
                "id=" + super.getId() +
                ", title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() + '\'' +
                ", parentEpicId=" + parentEpicId +
                ", startTime=" + super.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", duration=" + super.getDuration() +
                '}';
    }
}
