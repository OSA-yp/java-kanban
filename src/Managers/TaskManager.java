package Managers;
import Tasks.*;
import java.util.List;

public interface TaskManager {
    List<Task> getTasks();
    List<Epic> getEpics();
    List<SubTask> getSubTasks();

    List<Task> getHistory();

    //    b. �������� ���� �����.
    void removeAllTypesOfTasks();
    void removeAllTasks();

    // �������� ���� ������ � ��������� ���� �� ��������
    void removeAllEpics();
    // �������� ���� �������� � ���� ������ � ����������� �������� ������
    void removeAllSubTasks();
    //    c. ��������� �� ��������������.
    Task getTaskById(Integer id);
    SubTask getSubTaskById(Integer id);
    Epic getEpicById(Integer id);

    //    d. ��������. ��� ������ ������ ������������ � �������� ���������.
    void addTask(Task newTask);
    void addEpic(Epic newEpic);
    void addSubTask(SubTask newSubTask);

    //    e. ����������. ����� ������ ������� � ������ ��������������� ��������� � ���� ���������.
    boolean updateTask(Task task);
    boolean updateEpic(Epic epic);
    boolean updateSubTask(SubTask subTask);

    //    f. �������� �� ��������������.
    boolean removeTaskById(Integer id);
    boolean removeEpicById(Integer id);
    boolean removeSubTaskById(Integer id);

    // a. ��������� ������ ���� �������� ������������ �����.
    List<SubTask> getSubtaskByEpicId(int epicId);
}
