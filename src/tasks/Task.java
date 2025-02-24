package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task implements Comparable<Task> {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String title, String description, TaskStatus status, LocalDateTime startTime, long durationInMins) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(durationInMins);
        this.startTime = startTime;
    }

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = null;
        this.startTime = null;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task.Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                ", duration=" + duration.toMinutes() +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id != null ? id.equals(task.id) : task.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public int compareTo(Task o) {
        if (this.getStartTime() == null) {
            return -1;
        }
        if (o.getStartTime() == null) {
            return 1;
        }
        if (this.getStartTime() == null && o.getStartTime() == null) {
            return 0;
        }

        int result = this.getStartTime().compareTo(o.getStartTime());
        if (result == 0) {
            result = this.getId().compareTo(o.getId()); // если время совпадает, то сортировка по id
        }

        return result;
    }
}
