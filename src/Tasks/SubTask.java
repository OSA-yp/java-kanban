package Tasks;

public class SubTask extends Task {


    private final Integer parentEpicId;

    public SubTask(String title, String description, TaskStatus status, Integer parentEpicId) {
        super(title, description, status);
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
                '}';
    }
}
