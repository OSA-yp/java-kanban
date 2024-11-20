public class SubTask extends Task {


    private Integer parentEpicId;

    public SubTask(String title, String description, TaskStatus status, Integer parentEpicId) {
        super(title, description, status);
        this.parentEpicId = parentEpicId;
    }


    public Integer getParentEpicId() {
        return parentEpicId;
    }

    @Override
    void setStatus(TaskStatus status) {
        super.setStatus(status);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + super.getId() +
                ", title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() + '\'' +
                ", parentEpicId=" + parentEpicId +
                '}';
    }

    public void setParentEpicId(Integer parentEpicId) {
        this.parentEpicId = parentEpicId;
    }
}