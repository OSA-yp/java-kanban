public class SubTask extends Task {



    private Epic parentEpic;

    public SubTask(String title, String description) {
        super(title, description);
    }


    public Epic getParentEpic() {
        return parentEpic;
    }

    @Override
    void setStatus(TaskStatus status) {
        super.setStatus(status);
        parentEpic.refreshStatus();
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + super.getId() +
                ", title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() + '\'' +
                ", parentEpicId=" + parentEpic.getId() +
                '}';
    }

    public void setParentEpic(Epic parentEpic) {
        this.parentEpic = parentEpic;
    }
}
