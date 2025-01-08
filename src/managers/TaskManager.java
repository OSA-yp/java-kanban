package managers;

import tasks.*;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<Task> getHistory();

    //    b. Удаление всех задач.
    void removeAllTypesOfTasks();

    void removeAllTasks();

    // удаление всех эпиков с удалением всех их подзадач
    void removeAllEpics();

    // Удаление всех подзадач у всех эпиков с обновлением статусов эпиков
    void removeAllSubTasks();

    //    c. Получение по идентификатору.
    Task getTaskById(Integer id);

    SubTask getSubTaskById(Integer id);

    Epic getEpicById(Integer id);

    //    d. Создание. Сам объект должен передаваться в качестве параметра.
    void addTask(Task newTask);

    void addEpic(Epic newEpic);

    void addSubTask(SubTask newSubTask);

    //    e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    boolean updateTask(Task task);

    boolean updateEpic(Epic epic);

    boolean updateSubTask(SubTask subTask);

    //    f. Удаление по идентификатору.
    boolean removeTaskById(Integer id);

    boolean removeEpicById(Integer id);

    boolean removeSubTaskById(Integer id);

    // a. Получение списка всех подзадач определённого эпика.
    List<SubTask> getSubtaskByEpicId(int epicId);
}
