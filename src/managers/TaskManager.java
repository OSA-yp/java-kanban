package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface TaskManager {
    List<Task> getTasks();

    TreeSet<Task> getPrioritizedTasks();

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
    Optional<Task> getTaskById(Integer id);

    Optional<SubTask> getSubTaskById(Integer id);

    Optional<Epic> getEpicById(Integer id);

    //    d. Создание. Сам объект должен передаваться в качестве параметра.
    int addTask(Task newTask);

    int addEpic(Epic newEpic);

    int addSubTask(SubTask newSubTask);

    //    e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    int updateTask(Task task);

    int updateEpic(Epic epic);

    int updateSubTask(SubTask subTask);

    //    f. Удаление по идентификатору.
    int removeTaskById(Integer id);

    int removeEpicById(Integer id);

    int removeSubTaskById(Integer id);

    // a. Получение списка всех подзадач определённого эпика.
    List<SubTask> getSubtaskByEpicId(int epicId);
}
